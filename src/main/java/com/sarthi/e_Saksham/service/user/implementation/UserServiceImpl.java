package com.sarthi.e_Saksham.service.user.implementation;

import com.sarthi.e_Saksham.cache.CacheStore;
import com.sarthi.e_Saksham.constant.query.UserMstQuery;
import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;
import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import com.sarthi.e_Saksham.entity.security.SecurityConfigEntity;
import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import com.sarthi.e_Saksham.enumeration.UserLoginType;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import com.sarthi.e_Saksham.repository.user.UserMstRepository;
import com.sarthi.e_Saksham.security.principal.UserPrincipal;
import com.sarthi.e_Saksham.service.masters.role.RoleMstService;
import com.sarthi.e_Saksham.service.security.SecurityConfigService;
import com.sarthi.e_Saksham.service.user.UserService;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMstRepository userMstRepository;
    private final SecurityConfigService securityConfigService;
    private final RoleMstService roleMstService;
    private final CacheStore<String, Integer> userLoginCache;

    public UserServiceImpl(UserMstRepository userMstRepository, SecurityConfigService securityConfigService, RoleMstService roleMstService, CacheStore<String, Integer> userLoginCache) {
        this.userMstRepository = userMstRepository;
        this.securityConfigService = securityConfigService;
        this.roleMstService = roleMstService;
        this.userLoginCache = userLoginCache;
    }

    @Override
    @Transactional(readOnly = true)
    public UserMstEntity getUserMstEntityByUserName(String userName) throws UsernameNotFoundException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getUserMstEntityByUserName Method, LoggedInUser[{}], trying to fetch User by given UserName: {}", loggedInUser.userName(), userName);
        try {
            return this.userMstRepository.findByUserName(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for given username: " + userName));
        } catch (UsernameNotFoundException exception) {
            log.error("Inside getUserMstEntityByUserName Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Inside getUserMstEntityByUserName Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional
    public UserMstEntity updateLoginAttempt(String userName, UserLoginType userLoginType) throws UsernameNotFoundException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        RequestContext.setLoggedInUser(loggedInUser);
        log.info("Inside updateLoginAttempt Method, LoggedInUser[{}], trying to update the Login Attempt for user: {} and LoginType: {}", loggedInUser.userName(), userName, userLoginType.name());
        try {
            UserMstEntity userMstEntity = getUserMstEntityByUserName(userName);
            SecurityConfigEntity securityConfigEntity = securityConfigService.getSecurityConfigEntityByConfigId(1L);
            switch (userLoginType) {
                case LOGIN_ATTEMPT -> {
                    if (userLoginCache.get(userMstEntity.getUserName()) == null) {
                        userMstEntity.setLoginAttempts(0);
                        userMstEntity.setAccountNonLocked(true);
                    }
                    userMstEntity.setLoginAttempts(userMstEntity.getLoginAttempts() + 1);
                    userLoginCache.put(userMstEntity.getUserName(), userMstEntity.getLoginAttempts());
                    if (userLoginCache.get(userMstEntity.getUserName()) > securityConfigEntity.getMaxWrongPasswordAttempts()) {
                        userMstEntity.setAccountNonLocked(false);
                    }
                    log.info("Inside updateLoginAttempt Method, LoggedInUser[{}], LoginType: {}, Username: {}, loginAttempts: {}, AccountNonLocked: {}", loggedInUser.userName(), userLoginType.name(), userName, userMstEntity.getLoginAttempts(), userMstEntity.isAccountNonLocked());
                }
                case LOGIN_SUCCESS -> {
                    userMstEntity.setLoginAttempts(0);
                    userMstEntity.setAccountNonLocked(true);
                    userMstEntity.setLastLoginTimestamp(new Timestamp(System.currentTimeMillis()));
                    userLoginCache.evict(userMstEntity.getUserName());
                    log.info("Inside updateLoginAttempt Method, LoggedInUser[{}], LoginType: {}, Username: {}", loggedInUser.userName(), userLoginType.name(), userName);
                }
            }
            UserMstEntity savedUserMstEntity = this.userMstRepository.save(userMstEntity);
            log.info("Successfully updated {}", savedUserMstEntity);
            return savedUserMstEntity;
        } catch (Exception exception) {
            log.error("Inside updateLoginAttempt Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional
    public void resetLoginAttempts(String userName) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside resetLoginAttempts Method, LoggedInUser[{}], trying to Reset the Login Attempt for user: {}", loggedInUser.userName(), userName);
        try {
            Timestamp updatedAt = Timestamp.from(Instant.now());
            int updatedCount = this.userMstRepository.resetTheLoginAttemptByUsername(userName, updatedAt, loggedInUser.userId());
            if (updatedCount == 0) {
                log.warn("No user found to reset login attempts for username: {}", userName);
            } else if (updatedCount > 1) {
                log.error("Multiple rows affected! Check data integrity for username: {}", userName);
                throw new ESakshamApiException("Multiple users found with username: " + userName);
            } else {
                log.info("Successfully reset login attempts for user: {}", userName);
            }
        } catch (ESakshamApiException exception) {
            log.error("Inside resetLoginAttempts Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
        catch (Exception exception) {
            log.error("Inside resetLoginAttempts Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside loadUserByUsername Method, LoggedInUser[{}], trying to fetch User by given UserName: {}", loggedInUser.userName(), username);
        UserMstEntity userMstEntity = updateLoginAttempt(username, UserLoginType.LOGIN_ATTEMPT);
        String clientId = DataUtility.getClientIdFromRequest();
        if (Objects.isNull(clientId) || StringUtils.isEmpty(clientId) || StringUtils.isWhitespace(clientId)) {
            log.error("Client Id is: {}", clientId);
            throw new UsernameNotFoundException("Invalid request, Please try again");
        }
        RoleMstEntity roleMstEntity = this.roleMstService.getRoleMstEntityByUserIdAndClientId(userMstEntity.getUserId(), clientId);
        return new UserPrincipal(userMstEntity, roleMstEntity);
    }
}

package com.sarthi.e_Saksham.service.user.implementation;

import com.sarthi.e_Saksham.cache.CacheStore;
import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;
import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import com.sarthi.e_Saksham.enumeration.UserLoginType;
import com.sarthi.e_Saksham.repository.user.UserMstRepository;
import com.sarthi.e_Saksham.service.user.UserService;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMstRepository userMstRepository;
    private final CacheStore<String, Integer> userLoginCache;

    public UserServiceImpl(UserMstRepository userMstRepository, CacheStore<String, Integer> userLoginCache) {
        this.userMstRepository = userMstRepository;
        this.userLoginCache = userLoginCache;
    }

    @Override
    @Transactional(readOnly = true)
    public UserMstEntity getUserMstEntityByUserName(String userName) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getUserMstEntityByUserName Method, LoggedInUser[{}], trying to fetch User by given UserName: {}", loggedInUser.userName(), userName);
        try {
            return this.userMstRepository.findByUserNameIgnoreCase(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for given username: " + userName));
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

            switch (userLoginType) {
                case LOGIN_ATTEMPT -> {
                    if (userLoginCache.get(userMstEntity.getUserName()) == null) {
                        userMstEntity.setLoginAttempts(0);
                        userMstEntity.setAccountNonLocked(true);
                    }
                    userMstEntity.setLoginAttempts(userMstEntity.getLoginAttempts() + 1);
                    userLoginCache.put(userMstEntity.getUserName(), userMstEntity.getLoginAttempts());
                    if (userLoginCache.get(userMstEntity.getUserName()) > 5) {
                        userMstEntity.setAccountNonLocked(false);
                    }
                    log.info("Inside updateLoginAttempt Method, LoggedInUser[{}], LoginType: {}, Username: {}, loginAttempts: {}, AccountNonLocked: {}", loggedInUser.userName(), userLoginType.name(), userName, userMstEntity.getLoginAttempts(), userMstEntity.isAccountNonLocked());
                }
                case LOGIN_SUCCESS -> {
                    userMstEntity.setLoginAttempts(0);
                    userMstEntity.setAccountNonLocked(true);
                    userMstEntity.setLastLoginTimestamp(Timestamp.from(Instant.now()));
                    userLoginCache.evict(userMstEntity.getUserName());
                    log.info("Inside updateLoginAttempt Method, LoggedInUser[{}], LoginType: {}, Username: {}", loggedInUser.userName(), userLoginType.name(), userName);
                }
            }
            return userMstRepository.save(userMstEntity);
        } catch (Exception exception) {
            log.error("Inside updateLoginAttempt Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

}

package com.sarthi.e_Saksham.service.user.implementation;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import com.sarthi.e_Saksham.entity.user.UserRoleMappingEntity;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import com.sarthi.e_Saksham.repository.masters.role.RoleMstRepository;
import com.sarthi.e_Saksham.repository.user.UserRoleMappingRepository;
import com.sarthi.e_Saksham.service.user.UserRoleMappingService;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleMappingServiceImpl implements UserRoleMappingService {
    private static final Logger log = LoggerFactory.getLogger(UserRoleMappingServiceImpl.class);

    private final RoleMstRepository roleMstRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;

    public UserRoleMappingServiceImpl(RoleMstRepository roleMstRepository, UserRoleMappingRepository userRoleMappingRepository) {
        this.roleMstRepository = roleMstRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    @Override
    @Transactional
    public UserRoleMappingEntity createRoleForUserIdAndClientId(Long userId, String clientId) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside createRoleForUserIdAndClientId Method, LoggedInUser[{}], Creating new Role Mapping for UserId: {}, ClientId:{}", loggedInUser.userName(), userId, clientId);
        try {
            RoleMstEntity roleMstEntity = this.roleMstRepository.getDefaultRoleMstEntityByClientId(clientId)
                    .orElseThrow(() -> new ESakshamApiException("No default role found for clientId: " + clientId));
            UserRoleMappingEntity userRoleMappingEntity = new UserRoleMappingEntity();
            userRoleMappingEntity.setUserId(userId);
            userRoleMappingEntity.setClientId(clientId);
            userRoleMappingEntity.setRoleId(roleMstEntity.getRoleId());
            return this.userRoleMappingRepository.save(userRoleMappingEntity);
        } catch (ESakshamApiException exception) {
            log.error("Inside createRoleForUserIdAndClientId Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Inside createRoleForUserIdAndClientId Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }
}

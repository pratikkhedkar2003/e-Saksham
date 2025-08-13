package com.sarthi.e_Saksham.service.masters.role.implementation;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import com.sarthi.e_Saksham.entity.user.UserRoleMappingEntity;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import com.sarthi.e_Saksham.repository.masters.role.RoleMstRepository;
import com.sarthi.e_Saksham.service.masters.role.RoleMstService;
import com.sarthi.e_Saksham.service.user.UserRoleMappingService;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleMstServiceImpl implements RoleMstService {
    private static final Logger log = LoggerFactory.getLogger(RoleMstServiceImpl.class);

    private final RoleMstRepository roleMstRepository;
    private final UserRoleMappingService userRoleMappingService;

    public RoleMstServiceImpl(RoleMstRepository roleMstRepository, UserRoleMappingService userRoleMappingService) {
        this.roleMstRepository = roleMstRepository;
        this.userRoleMappingService = userRoleMappingService;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleMstEntity getDefaultRoleMstEntityByClientId(String clientId) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getDefaultRoleMstEntityByClientId Method, LoggedInUser[{}], trying to fetch Default Role by given ClientId: {}", loggedInUser.userName(), clientId);
        try {
            return this.roleMstRepository.getDefaultRoleMstEntityByClientId(clientId)
                    .orElseThrow(() -> new ESakshamApiException("No default role found for clientId: " + clientId));
        } catch (ESakshamApiException exception) {
            log.error("Inside getDefaultRoleMstEntityByClientId Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Inside getDefaultRoleMstEntityByClientId Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMstEntity> getAllRoleMstEntitiesByClientId(String clientId) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getAllRoleMstEntitiesByClientId Method, LoggedInUser[{}], trying to fetch All Roles by given ClientId: {}", loggedInUser.userName(), clientId);
        try {
            return this.roleMstRepository.findAllByClientId(clientId);
        } catch (Exception exception) {
            log.error("Inside getAllRoleMstEntitiesByClientId Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RoleMstEntity getRoleMstEntityByUserIdAndClientId(Long userId, String clientId) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getRoleMstEntityByUserIdAndClientId Method, LoggedInUser[{}], trying to fetch Role by given UserId: {} and ClientId: {}", loggedInUser.userName(), userId, clientId);
        try {
            Optional<RoleMstEntity> roleMstEntityOptional = this.roleMstRepository.getRoleMstEntityByUserIdAndClientId(userId, clientId);
            if (roleMstEntityOptional.isPresent()) {
                log.info("Inside getRoleMstEntityByUserIdAndClientId Method, LoggedInUser[{}], Role Found for UserId: {}, Role: {}", loggedInUser.userName(), userId, roleMstEntityOptional.get().getRoleName());
                return roleMstEntityOptional.get();
            }
            log.info("Inside getRoleMstEntityByUserIdAndClientId Method, LoggedInUser[{}], Role Not Found for UserId: {} and ClientId: {}", loggedInUser.userName(), userId, clientId);
            UserRoleMappingEntity userRoleMappingEntity = this.userRoleMappingService.createRoleForUserIdAndClientId(userId, clientId);
            return this.roleMstRepository.findById(userRoleMappingEntity.getRoleId())
                    .orElseThrow(() -> new ESakshamApiException("No role found by id: " + userRoleMappingEntity.getRoleId()));
        } catch (ESakshamApiException exception) {
            log.error("Inside getRoleMstEntityByUserIdAndClientId Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Inside getRoleMstEntityByUserIdAndClientId Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }

}

package com.sarthi.e_Saksham.service.security.implementation;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.entity.security.SecurityConfigEntity;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import com.sarthi.e_Saksham.repository.security.SecurityConfigRepository;
import com.sarthi.e_Saksham.service.security.SecurityConfigService;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityConfigServiceImpl implements SecurityConfigService {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfigServiceImpl.class);

    private final SecurityConfigRepository securityConfigRepository;

    public SecurityConfigServiceImpl(SecurityConfigRepository securityConfigRepository) {
        this.securityConfigRepository = securityConfigRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityConfigEntity getSecurityConfigEntityByConfigId(Long configId) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getSecurityConfigEntityByConfigId Method, LoggedInUser[{}], trying to fetch SecurityConfig by given ConfigId: {}", loggedInUser.userName(), configId);
        try {
            return this.securityConfigRepository.findById(configId)
                    .orElseThrow(() -> new ESakshamApiException("SecurityConfig not found for given id: " + configId));
        } catch (ESakshamApiException exception) {
            log.error("Inside getSecurityConfigEntityByConfigId Method, LoggedInUser[{}], Error occurred : {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Inside getSecurityConfigEntityByConfigId Method, LoggedInUser[{}], Error occurred: {}", loggedInUser.userName(), exception.getMessage());
            throw exception;
        }
    }
}

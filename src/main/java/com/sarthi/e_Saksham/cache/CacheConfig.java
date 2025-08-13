package com.sarthi.e_Saksham.cache;

import com.sarthi.e_Saksham.entity.security.SecurityConfigEntity;
import com.sarthi.e_Saksham.service.security.SecurityConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    private final SecurityConfigService securityConfigService;

    public CacheConfig(SecurityConfigService securityConfigService) {
        this.securityConfigService = securityConfigService;
    }

    @Bean
    public CacheStore<String, Integer> userLoginCache() {
        SecurityConfigEntity securityConfigEntity = securityConfigService.getSecurityConfigEntityByConfigId(1L);
        return new CacheStore<>(securityConfigEntity.getAccountLockDurationMinutes(), TimeUnit.MINUTES);
    }

}

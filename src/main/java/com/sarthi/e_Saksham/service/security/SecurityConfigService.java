package com.sarthi.e_Saksham.service.security;

import com.sarthi.e_Saksham.entity.security.SecurityConfigEntity;

public interface SecurityConfigService {

    SecurityConfigEntity getSecurityConfigEntityByConfigId(Long configId);

}

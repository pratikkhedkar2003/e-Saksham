package com.sarthi.e_Saksham.service.masters.role;

import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;

import java.util.List;

public interface RoleMstService {

    RoleMstEntity getDefaultRoleMstEntityByClientId(String clientId);

    List<RoleMstEntity> getAllRoleMstEntitiesByClientId(String clientId);

    RoleMstEntity getRoleMstEntityByUserIdAndClientId(Long userId, String clientId);
}

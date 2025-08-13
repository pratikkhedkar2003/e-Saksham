package com.sarthi.e_Saksham.service.user;

import com.sarthi.e_Saksham.entity.user.UserRoleMappingEntity;

public interface UserRoleMappingService {

    UserRoleMappingEntity createRoleForUserIdAndClientId(Long userId, String clientId);

}

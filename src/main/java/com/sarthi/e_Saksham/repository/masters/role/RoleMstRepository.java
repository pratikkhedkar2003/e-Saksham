package com.sarthi.e_Saksham.repository.masters.role;

import com.sarthi.e_Saksham.constant.query.RoleMstQuery;
import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleMstRepository extends JpaRepository<RoleMstEntity, Long> {

    List<RoleMstEntity> findAllByClientId(String clientId);

    @Query(value = RoleMstQuery.SELECT_DEFAULT_ROLE_BY_CLIENT_ID, nativeQuery = true)
    Optional<RoleMstEntity> getDefaultRoleMstEntityByClientId(@Param(value = "clientId") String clientId);

    @Query(value = RoleMstQuery.SELECT_ROLE_BY_USER_ID_AND_CLIENT_ID, nativeQuery = true)
    Optional<RoleMstEntity> getRoleMstEntityByUserIdAndClientId(@Param(value = "userId") Long userId, @Param(value = "clientId") String clientId);

}

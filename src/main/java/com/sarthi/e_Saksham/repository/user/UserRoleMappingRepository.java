package com.sarthi.e_Saksham.repository.user;

import com.sarthi.e_Saksham.entity.user.UserRoleMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMappingEntity, Long> {
}

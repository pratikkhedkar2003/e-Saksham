package com.sarthi.e_Saksham.repository.masters.role;

import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMstRepository extends JpaRepository<RoleMstEntity, Long> {
}

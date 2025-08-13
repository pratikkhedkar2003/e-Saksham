package com.sarthi.e_Saksham.repository.security;

import com.sarthi.e_Saksham.entity.security.SecurityConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityConfigRepository extends JpaRepository<SecurityConfigEntity, Long> {
}

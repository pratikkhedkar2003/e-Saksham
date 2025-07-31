package com.sarthi.e_Saksham.repository.user;

import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMstRepository extends JpaRepository<UserMstEntity, Long> {
}

package com.sarthi.e_Saksham.repository.user;

import com.sarthi.e_Saksham.constant.query.UserMstQuery;
import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface UserMstRepository extends JpaRepository<UserMstEntity, Long> {

    Optional<UserMstEntity> findByUserName(String userName);

    boolean existsByUserNameIgnoreCase(String userName);

    @Modifying
    @Transactional
    @Query(value = UserMstQuery.UPDATE_RESET_LOGIN_ATTEMPT_BY_USERNAME, nativeQuery = true)
    int resetTheLoginAttemptByUsername(@Param(value = "username") String userName, @Param(value = "updatedAt") Timestamp updatedAt, @Param(value = "updatedBy") Long updatedBy);

}

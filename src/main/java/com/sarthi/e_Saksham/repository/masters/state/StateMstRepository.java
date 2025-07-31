package com.sarthi.e_Saksham.repository.masters.state;

import com.sarthi.e_Saksham.entity.masters.state.StateMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMstRepository extends JpaRepository<StateMstEntity, Long> {
}

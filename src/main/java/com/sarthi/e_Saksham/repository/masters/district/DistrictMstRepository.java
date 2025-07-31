package com.sarthi.e_Saksham.repository.masters.district;

import com.sarthi.e_Saksham.entity.masters.district.DistrictMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictMstRepository extends JpaRepository<DistrictMstEntity, Long> {
}

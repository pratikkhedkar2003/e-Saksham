package com.sarthi.e_Saksham.repository.masters.country;

import com.sarthi.e_Saksham.entity.masters.country.CountryMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryMstRepository extends JpaRepository<CountryMstEntity, Long> {
}

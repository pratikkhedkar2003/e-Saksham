package com.sarthi.e_Saksham.entity.masters.district;

import com.sarthi.e_Saksham.entity.Auditable;
import com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "esaksham_districts_mst")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class DistrictMstEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Id
    @Column(name = "district_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_districts_mst_district_id_seq_gen", sequenceName = "esaksham_districts_mst_district_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_districts_mst_district_id_seq_gen")
    private Long districtId;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "state_id", nullable = false)
    private Long stateId;

    @Column(name = "country_id", nullable = false)
    private Long countryId;

    public DistrictMstEntity() {
        super();
    }

    public DistrictMstEntity(Long districtId, String districtName, boolean active, Long stateId, Long countryId) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.active = active;
        this.stateId = stateId;
        this.countryId = countryId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "DistrictMstEntity{" +
                "districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                ", active=" + active +
                ", stateId=" + stateId +
                ", countryId=" + countryId +
                '}';
    }
}

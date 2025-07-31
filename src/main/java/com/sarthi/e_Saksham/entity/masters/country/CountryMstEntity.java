package com.sarthi.e_Saksham.entity.masters.country;

import com.sarthi.e_Saksham.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "esaksham_countries_mst")
public class CountryMstEntity extends Auditable {
    @Id
    @Column(name = "country_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_countries_mst_country_id_seq_gen", sequenceName = "esaksham_countries_mst_country_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_countries_mst_country_id_seq_gen")
    private Long countryId;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "is_active")
    private boolean active;

    public CountryMstEntity() {
        super();
    }

    public CountryMstEntity(Long countryId, String countryName, boolean active) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.active = active;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "CountryMstEntity{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", active=" + active +
                '}';
    }
}

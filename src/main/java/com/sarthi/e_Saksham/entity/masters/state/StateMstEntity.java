package com.sarthi.e_Saksham.entity.masters.state;

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
@Table(name = "esaksham_states_mst")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class StateMstEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Id
    @Column(name = "state_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_states_mst_state_id_seq_gen", sequenceName = "esaksham_states_mst_state_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_states_mst_state_id_seq_gen")
    private Long stateId;

    @Column(name = "state_name", nullable = false)
    private String stateName;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "country_id", nullable = false)
    private Long countryId;

    public StateMstEntity() {
        super();
    }

    public StateMstEntity(Long stateId, String stateName, boolean active, Long countryId) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.active = active;
        this.countryId = countryId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "StateMstEntity{" +
                "stateId=" + stateId +
                ", stateName='" + stateName + '\'' +
                ", active=" + active +
                ", countryId=" + countryId +
                '}';
    }
}

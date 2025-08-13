package com.sarthi.e_Saksham.entity.user;

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
@Table(name = "esaksham_user_role_mappings")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class UserRoleMappingEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Id
    @Column(name = "user_role_mapping_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_user_role_mappings_user_role_mapping_id_seq_gen", sequenceName = "esaksham_user_role_mappings_user_role_mapping_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_user_role_mappings_user_role_mapping_id_seq_gen")
    private Long userRoleMappingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    public UserRoleMappingEntity() {
        super();
    }

    public UserRoleMappingEntity(Long userRoleMappingId, Long userId, Long roleId, String clientId) {
        this.userRoleMappingId = userRoleMappingId;
        this.userId = userId;
        this.roleId = roleId;
        this.clientId = clientId;
    }

    public Long getUserRoleMappingId() {
        return userRoleMappingId;
    }

    public void setUserRoleMappingId(Long userRoleMappingId) {
        this.userRoleMappingId = userRoleMappingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "UserRoleMappingEntity{" +
                "userRoleMappingId=" + userRoleMappingId +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", clientId=" + clientId +
                '}';
    }
}

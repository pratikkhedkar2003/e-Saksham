package com.sarthi.e_Saksham.entity.masters.role;

import com.sarthi.e_Saksham.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "esaksham_roles_mst")
public class RoleMstEntity extends Auditable {
    @Id
    @Column(name = "role_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_roles_mst_role_id_seq_gen", sequenceName = "esaksham_roles_mst_role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_roles_mst_role_id_seq_gen")
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;

    @Column(name = "is_default")
    private boolean defaultRole;

    public RoleMstEntity() {
        super();
    }

    public RoleMstEntity(Long roleId, String roleName, String authorities, String clientId, boolean defaultRole) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.authorities = authorities;
        this.clientId = clientId;
        this.defaultRole = defaultRole;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(boolean defaultRole) {
        this.defaultRole = defaultRole;
    }

    @Override
    public String toString() {
        return "RoleMstEntity{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", authorities='" + authorities + '\'' +
                ", clientId='" + clientId + '\'' +
                ", defaultRole='" + defaultRole + '\'' +
                '}';
    }
}

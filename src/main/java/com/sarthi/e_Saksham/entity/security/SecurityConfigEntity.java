package com.sarthi.e_Saksham.entity.security;

import com.sarthi.e_Saksham.entity.Auditable;
import com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "esaksham_security_config")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class SecurityConfigEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Id
    @Column(name = "config_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_security_config_config_id_seq_gen", sequenceName = "esaksham_security_config_config_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_security_config_config_id_seq_gen")
    private Long configId;

    @Column(name = "jwt_expiry_minutes")
    private Integer jwtExpiryMinutes;

    @Column(name = "session_expiry_minutes")
    private Integer sessionExpiryMinutes;

    @Column(name = "otp_expiry_minutes")
    private Integer otpExpiryMinutes;

    @Column(name = "max_wrong_password_attempts")
    private Integer maxWrongPasswordAttempts;

    @Column(name = "password_change_cycle_days")
    private Integer passwordChangeCycleDays;

    @Column(name = "account_lock_duration_minutes")
    private Integer accountLockDurationMinutes;

    @Column(name = "session_inactive_timeout_minutes")
    private Integer sessionInactiveTimeoutMinutes;

    @Column(name = "session_allowed")
    private boolean sessionAllowed;

    public SecurityConfigEntity() {
        super();
    }

    public SecurityConfigEntity(Long configId, Integer jwtExpiryMinutes, Integer sessionExpiryMinutes,
                                Integer otpExpiryMinutes, Integer maxWrongPasswordAttempts, Integer passwordChangeCycleDays,
                                Integer accountLockDurationMinutes, Integer sessionInactiveTimeoutMinutes, boolean sessionAllowed
    ) {
        this.configId = configId;
        this.jwtExpiryMinutes = jwtExpiryMinutes;
        this.sessionExpiryMinutes = sessionExpiryMinutes;
        this.otpExpiryMinutes = otpExpiryMinutes;
        this.maxWrongPasswordAttempts = maxWrongPasswordAttempts;
        this.passwordChangeCycleDays = passwordChangeCycleDays;
        this.accountLockDurationMinutes = accountLockDurationMinutes;
        this.sessionInactiveTimeoutMinutes = sessionInactiveTimeoutMinutes;
        this.sessionAllowed = sessionAllowed;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Integer getJwtExpiryMinutes() {
        return jwtExpiryMinutes;
    }

    public void setJwtExpiryMinutes(Integer jwtExpiryMinutes) {
        this.jwtExpiryMinutes = jwtExpiryMinutes;
    }

    public Integer getSessionExpiryMinutes() {
        return sessionExpiryMinutes;
    }

    public void setSessionExpiryMinutes(Integer sessionExpiryMinutes) {
        this.sessionExpiryMinutes = sessionExpiryMinutes;
    }

    public Integer getOtpExpiryMinutes() {
        return otpExpiryMinutes;
    }

    public void setOtpExpiryMinutes(Integer otpExpiryMinutes) {
        this.otpExpiryMinutes = otpExpiryMinutes;
    }

    public Integer getMaxWrongPasswordAttempts() {
        return maxWrongPasswordAttempts;
    }

    public void setMaxWrongPasswordAttempts(Integer maxWrongPasswordAttempts) {
        this.maxWrongPasswordAttempts = maxWrongPasswordAttempts;
    }

    public Integer getPasswordChangeCycleDays() {
        return passwordChangeCycleDays;
    }

    public void setPasswordChangeCycleDays(Integer passwordChangeCycleDays) {
        this.passwordChangeCycleDays = passwordChangeCycleDays;
    }

    public Integer getAccountLockDurationMinutes() {
        return accountLockDurationMinutes;
    }

    public void setAccountLockDurationMinutes(Integer accountLockDurationMinutes) {
        this.accountLockDurationMinutes = accountLockDurationMinutes;
    }

    public Integer getSessionInactiveTimeoutMinutes() {
        return sessionInactiveTimeoutMinutes;
    }

    public void setSessionInactiveTimeoutMinutes(Integer sessionInactiveTimeoutMinutes) {
        this.sessionInactiveTimeoutMinutes = sessionInactiveTimeoutMinutes;
    }

    public boolean isSessionAllowed() {
        return sessionAllowed;
    }

    public void setSessionAllowed(boolean sessionAllowed) {
        this.sessionAllowed = sessionAllowed;
    }

    @Override
    public String toString() {
        return "SecurityConfigEntity{" +
                "configId=" + configId +
                ", jwtExpiryMinutes=" + jwtExpiryMinutes +
                ", sessionExpiryMinutes=" + sessionExpiryMinutes +
                ", otpExpiryMinutes=" + otpExpiryMinutes +
                ", maxWrongPasswordAttempts=" + maxWrongPasswordAttempts +
                ", passwordChangeCycleDays=" + passwordChangeCycleDays +
                ", accountLockDurationMinutes=" + accountLockDurationMinutes +
                ", sessionInactiveTimeoutMinutes=" + sessionInactiveTimeoutMinutes +
                ", sessionAllowed=" + sessionAllowed +
                '}';
    }
}

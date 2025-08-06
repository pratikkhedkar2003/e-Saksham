package com.sarthi.e_Saksham.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "esaksham_users_mst")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class UserMstEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Id
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "esaksham_users_mst_user_id_seq_gen", sequenceName = "esaksham_users_mst_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esaksham_users_mst_user_id_seq_gen")
    private Long userId;

    @Column(name = "esaksham_id", nullable = false, unique = true, updatable = false)
    private String esakshamId;

    @Column(name = "user_name", nullable = false, unique = true, length = 100)
    private String userName;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", nullable = false, length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "mobile_no", nullable = false, unique = true, length = 20)
    private String mobileNo;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "user_image")
    private byte[] userImage;

    @Column(name = "registration_timestamp")
    private Timestamp registrationTimestamp;

    @Column(name = "last_login_timestamp")
    private Timestamp lastLoginTimestamp;

    @Column(name = "password_expiration_timestamp")
    private Timestamp passwordExpirationTimestamp;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @JsonIgnore
    @Column(name = "qr_code_secret")
    private String qrCodeSecret;

    @Column(name = "qr_code_image_uri")
    private String qrCodeImageUri;

    @Column(name = "mfa")
    private boolean mfa;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    public UserMstEntity() {
        super();
    }

    public UserMstEntity(Long userId, String esakshamId, String userName, String firstName, String middleName,
                         String lastName, String email, String password, String mobileNo, String gender,
                         Date dateOfBirth, byte[] userImage, Timestamp registrationTimestamp, Timestamp lastLoginTimestamp,
                         Timestamp passwordExpirationTimestamp, Integer loginAttempts, String qrCodeSecret,
                         String qrCodeImageUri, boolean mfa, boolean enabled, boolean accountNonExpired, boolean accountNonLocked)
    {
        this.userId = userId;
        this.esakshamId = esakshamId;
        this.userName = userName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.userImage = userImage;
        this.registrationTimestamp = registrationTimestamp;
        this.lastLoginTimestamp = lastLoginTimestamp;
        this.passwordExpirationTimestamp = passwordExpirationTimestamp;
        this.loginAttempts = loginAttempts;
        this.qrCodeSecret = qrCodeSecret;
        this.qrCodeImageUri = qrCodeImageUri;
        this.mfa = mfa;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEsakshamId() {
        return esakshamId;
    }

    public void setEsakshamId(String esakshamId) {
        this.esakshamId = esakshamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public Timestamp getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(Timestamp registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public Timestamp getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(Timestamp lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    public Timestamp getPasswordExpirationTimestamp() {
        return passwordExpirationTimestamp;
    }

    public void setPasswordExpirationTimestamp(Timestamp passwordExpirationTimestamp) {
        this.passwordExpirationTimestamp = passwordExpirationTimestamp;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getQrCodeSecret() {
        return qrCodeSecret;
    }

    public void setQrCodeSecret(String qrCodeSecret) {
        this.qrCodeSecret = qrCodeSecret;
    }

    public String getQrCodeImageUri() {
        return qrCodeImageUri;
    }

    public void setQrCodeImageUri(String qrCodeImageUri) {
        this.qrCodeImageUri = qrCodeImageUri;
    }

    public boolean isMfa() {
        return mfa;
    }

    public void setMfa(boolean mfa) {
        this.mfa = mfa;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public String toString() {
        return "UserMst{" +
                "userId=" + userId +
                ", esakshamId='" + esakshamId + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", registrationTimestamp=" + registrationTimestamp +
                ", lastLoginTimestamp=" + lastLoginTimestamp +
                ", loginAttempts=" + loginAttempts +
                ", passwordExpirationTimestamp=" + passwordExpirationTimestamp +
                ", qrCodeSecret='" + qrCodeSecret + '\'' +
                ", qrCodeImageUri='" + qrCodeImageUri + '\'' +
                ", mfa=" + mfa +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }
}

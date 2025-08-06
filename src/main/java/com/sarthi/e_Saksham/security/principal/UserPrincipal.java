package com.sarthi.e_Saksham.security.principal;

import com.sarthi.e_Saksham.entity.masters.role.RoleMstEntity;
import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

import static com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant.AUTH_DELIMITER;
import static com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant.ROLE_PREFIX;

public record UserPrincipal(
        UserMstEntity userMstEntity,
        RoleMstEntity roleMstEntity
) implements UserDetails, CredentialsContainer {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(new StringJoiner(AUTH_DELIMITER)
                .add(ROLE_PREFIX + roleMstEntity.getRoleName())
                .add(roleMstEntity.getAuthorities())
                .toString()
        );
    }

    @Override
    public String getPassword() {
        return this.userMstEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userMstEntity().getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.userMstEntity.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userMstEntity.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Objects.nonNull(this.userMstEntity.getPasswordExpirationTimestamp()) &&
                this.userMstEntity.getPasswordExpirationTimestamp().after(Date.from(Instant.now()));
    }

    @Override
    public boolean isEnabled() {
        return this.userMstEntity.isEnabled();
    }

    @Override
    public void eraseCredentials() {
        this.userMstEntity.setPassword(null);
        this.userMstEntity.setQrCodeSecret(null);
    }
}

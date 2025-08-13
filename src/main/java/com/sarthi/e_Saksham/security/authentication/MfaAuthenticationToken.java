package com.sarthi.e_Saksham.security.authentication;

import com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@SuppressWarnings("LombokGetterMayBeUsed")
public class MfaAuthenticationToken extends AnonymousAuthenticationToken {
    private final Authentication primaryAuthentication;

    public MfaAuthenticationToken(Authentication primaryAuthentication, String mfaAuthority) {
        super(ESakshamAuthConstant.ANONYMOUS, ESakshamAuthConstant.ANONYMOUS, AuthorityUtils.createAuthorityList(ESakshamAuthConstant.ROLE_ANONYMOUS, mfaAuthority));
        this.primaryAuthentication = primaryAuthentication;
    }

    public Authentication getPrimaryAuthentication() {
        return primaryAuthentication;
    }
}

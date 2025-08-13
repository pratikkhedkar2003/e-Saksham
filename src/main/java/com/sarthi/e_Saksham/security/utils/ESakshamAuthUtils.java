package com.sarthi.e_Saksham.security.utils;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.security.authentication.MfaAuthenticationToken;
import com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant;
import com.sarthi.e_Saksham.security.principal.UserPrincipal;
import com.sarthi.e_Saksham.utils.DataUtility;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.stream.Collectors;

public final class ESakshamAuthUtils {

    private ESakshamAuthUtils() {
        throw new AssertionError("No com.sarthi.e_Saksham.security.utils.ESakshamAuthUtils instances for you!");
    }

    public static UserPrincipal getUserPrincipal(Authentication authentication) {
        if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
            return (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    public static LoggedInUser getLoggedInUserFromAuthentication(Authentication authentication) {

        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        Object principal = getPrincipalFromAuthentication(authentication);
        if (principal instanceof UserPrincipal userPrincipal) {
            return new LoggedInUser(
                    userPrincipal.userMstEntity().getUserId(),
                    userPrincipal.userMstEntity().getUserName()
            );
        }
        return DataUtility.getDefaultSystemLoggedInUser();
    }

    public static String getUserAuthorities(JwtEncodingContext context) {
        return context.getPrincipal().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(ESakshamAuthConstant.AUTH_DELIMITER));
    }

    private static Object getPrincipalFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken oAuth2AuthorizationCodeRequestAuthenticationToken) {
            principal = oAuth2AuthorizationCodeRequestAuthenticationToken.getPrincipal();
        } else if (authentication instanceof MfaAuthenticationToken mfaAuthenticationToken) {
            principal = mfaAuthenticationToken.getPrimaryAuthentication();
        }

        if (principal instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            principal = usernamePasswordAuthenticationToken.getPrincipal();
        }
        return principal;
    }

}

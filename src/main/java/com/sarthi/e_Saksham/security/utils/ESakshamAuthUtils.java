package com.sarthi.e_Saksham.security.utils;

import com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant;
import com.sarthi.e_Saksham.security.principal.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.stream.Collectors;

public class ESakshamAuthUtils {

    public static UserPrincipal getUserPrincipal(Authentication authentication) {
        if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
            return (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    public static String getUserAuthorities(JwtEncodingContext context) {
        return context.getPrincipal().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(ESakshamAuthConstant.AUTH_DELIMITER));
    }

}

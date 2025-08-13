package com.sarthi.e_Saksham.security.handler;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.enumeration.UserLoginType;
import com.sarthi.e_Saksham.security.authentication.MfaAuthenticationToken;
import com.sarthi.e_Saksham.security.principal.UserPrincipal;
import com.sarthi.e_Saksham.security.utils.ESakshamAuthUtils;
import com.sarthi.e_Saksham.service.user.UserService;
import com.sarthi.e_Saksham.utils.DataUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

public class MfaAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(MfaAuthenticationSuccessHandler.class);

    private final UserService userService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AuthenticationSuccessHandler mfaNotEnabledSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final String mfaAuthority;

    public MfaAuthenticationSuccessHandler(String successUrl, String mfaAuthority, UserService userService) {
        SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler(successUrl);
        simpleUrlAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
        this.authenticationSuccessHandler = simpleUrlAuthenticationSuccessHandler;
        this.userService = userService;
        this.mfaAuthority = mfaAuthority;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        UserPrincipal userPrincipal = ESakshamAuthUtils.getUserPrincipal(authentication);
        log.info("Inside onAuthenticationSuccess Method, LoggedInUser[{}], trying to check MFA for user: {}", loggedInUser.userName(), userPrincipal.getUsername());
        if (!(userPrincipal.userMstEntity().isMfa())) {
            userService.updateLoginAttempt(userPrincipal.getUsername(), UserLoginType.LOGIN_SUCCESS);
            mfaNotEnabledSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        saveAuthentication(request, response, new MfaAuthenticationToken(authentication, mfaAuthority));
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    private void saveAuthentication(HttpServletRequest request, HttpServletResponse response, MfaAuthenticationToken mfaAuthenticationToken) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(mfaAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
    }

}

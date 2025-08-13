package com.sarthi.e_Saksham.controller;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.enumeration.UserLoginType;
import com.sarthi.e_Saksham.security.authentication.MfaAuthenticationToken;
import com.sarthi.e_Saksham.security.principal.UserPrincipal;
import com.sarthi.e_Saksham.service.user.UserService;
import com.sarthi.e_Saksham.utils.DataUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/mfa?error");
    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/login")
    public String getLoginPage() {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside getLoginPage Method, LoggedInUser[{}], getting Login Page", loggedInUser.userName());
        return "login/login";
    }

    @GetMapping("/mfa")
    public String mfaLoginPage(Model model, @CurrentSecurityContext SecurityContext securityContext) {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside mfaLoginPage Method, LoggedInUser[{}], getting Mfa Page", loggedInUser.userName());
        UserPrincipal userPrincipal = getAuthenticatedUser(securityContext.getAuthentication());
        model.addAttribute("username", userPrincipal.userMstEntity().getUserName());
        userService.resetLoginAttempts(userPrincipal.userMstEntity().getUserName());
        return "login/mfa";
    }

    @PostMapping("/mfa")
    public void validateMfaCode(
            @RequestParam(value = "code") String code,
            HttpServletRequest request,
            HttpServletResponse response,
            @CurrentSecurityContext SecurityContext securityContext
    ) throws ServletException, IOException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        UserPrincipal userPrincipal = getAuthenticatedUser(securityContext.getAuthentication());
        log.info("Inside validateMfaCode Method, LoggedInUser[{}], validating MFA Code for User: {}", loggedInUser.userName(), userPrincipal.getUsername());
        if (userPrincipal.getUsername().equalsIgnoreCase("e-Saksham-user") && code.equals("134679")) {
            userService.updateLoginAttempt(userPrincipal.getUsername(), UserLoginType.LOGIN_SUCCESS);
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, getSavedAuthentication(request, response));
            return;
        }
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid QR code. Please try again."));
    }

    private Authentication getSavedAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        MfaAuthenticationToken mfaAuthenticationToken = (MfaAuthenticationToken) securityContext.getAuthentication();
        securityContext.setAuthentication(mfaAuthenticationToken.getPrimaryAuthentication());
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
        return mfaAuthenticationToken.getPrimaryAuthentication();
    }

    private UserPrincipal getAuthenticatedUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
        {
            return (UserPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

}

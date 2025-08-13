package com.sarthi.e_Saksham.security.handler;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant;
import com.sarthi.e_Saksham.utils.DataUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(LoginFailureHandler.class);

    private String defaultFailureUrl;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LoginFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LoggedInUser loggedInUser = DataUtility.getLoggedInUser();
        log.info("Inside onAuthenticationFailure Method, LoggedInUser[{}], Error Occurred: {}", loggedInUser.userName(), exception.getMessage());
        String errorMessage;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            errorMessage = "Invalid email and/or password.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Account is currently disabled.";
        } else if (exception instanceof LockedException) {
            errorMessage = "Account is currently locked.";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "Account is currently expired.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Credentials expired. Please reset your credentials.";
        } else if (exception instanceof ESakshamApiException) {
            errorMessage = exception.getMessage();
        } else {
            errorMessage = "An error has occurred. Please try again.";
        }
        saveException(request, errorMessage);
        this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
    }

    protected final void saveException(HttpServletRequest request, String errorMessage) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.getSession().setAttribute(ESakshamAuthConstant.AUTHENTICATION_EXCEPTION, errorMessage);
        } else {
            request.setAttribute(ESakshamAuthConstant.AUTHENTICATION_EXCEPTION, errorMessage);
        }
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                () -> "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }
}

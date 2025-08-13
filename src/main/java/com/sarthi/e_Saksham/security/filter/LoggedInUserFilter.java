package com.sarthi.e_Saksham.security.filter;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;
import com.sarthi.e_Saksham.security.utils.ESakshamAuthUtils;
import com.sarthi.e_Saksham.utils.DataUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class LoggedInUserFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggedInUserFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("Inside LoggedInUserFilter, doFilterInternal Method");
        RequestContext.clear();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || authentication instanceof AnonymousAuthenticationToken) {
                LoggedInUser loggedInUser = DataUtility.getDefaultSystemLoggedInUser();
                RequestContext.setLoggedInUser(loggedInUser);
                log.warn("Inside LoggedInUserFilter, doFilterInternal Method, No authentication found. Setting system-level LoggedInUser[userId:{}, username:{}]", loggedInUser.userId(), loggedInUser.userName());
            } else {
                LoggedInUser loggedInUser = ESakshamAuthUtils.getLoggedInUserFromAuthentication(authentication);
                RequestContext.setLoggedInUser(loggedInUser);
                log.info("Inside LoggedInUserFilter, doFilterInternal Method, LoggedInUser[userId:{}, username:{}]", loggedInUser.userId(), loggedInUser.userName());
            }
            filterChain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }
}

package com.sarthi.e_Saksham.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestContext {
    private static final Logger log = LoggerFactory.getLogger(RequestContext.class);

    private static final ThreadLocal<LoggedInUser> LOGGED_IN_USER = new ThreadLocal<>();

    private RequestContext() {
        throw new AssertionError("No com.sarthi.e_Saksham.domain.RequestContext instances for you!");
    }

    /**
     * @deprecated Use {@link #clear()} instead. This method will be removed in a future version.
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    public static void start() {
        log.info("Deprecated method start() called. Use clear() instead.");
        clear();
    }

    public static void clear() {
        log.info("Clearing LoggedInUser from RequestContext");
        LOGGED_IN_USER.remove();
    }

    public static void setLoggedInUser(LoggedInUser loggedInUser) {
        log.info("Inside setLoggedInUser Method, trying to set Given LoggedInUser: {} in RequestContext", loggedInUser.userName());
        LOGGED_IN_USER.set(loggedInUser);
    }

    public static LoggedInUser getLoggedInUser() {
        log.info("Inside getLoggedInUser Method, trying to get Given LoggedInUser from RequestContext");
        return LOGGED_IN_USER.get();
    }
}

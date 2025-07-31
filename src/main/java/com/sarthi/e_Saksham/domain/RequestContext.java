package com.sarthi.e_Saksham.domain;

public class RequestContext {
    private static final ThreadLocal<LoggedInUser> LOGGED_IN_USER = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void start() {
        LOGGED_IN_USER.remove();
    }

    public static void setLoggedInUser(LoggedInUser loggedInUser) {
        LOGGED_IN_USER.set(loggedInUser);
    }

    public static LoggedInUser getLoggedInUser() {
        return LOGGED_IN_USER.get();
    }
}

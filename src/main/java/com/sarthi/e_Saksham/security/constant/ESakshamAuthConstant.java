package com.sarthi.e_Saksham.security.constant;

public final class ESakshamAuthConstant {

    public static final String APPLICATION_BASE_URL = "http://localhost:8081/";

    public static final String ISSUER = "http://e-Saksham.sarthi.com";

    public static final int PASSWORD_STRENGTH = 12;

    public static final String AUTHORITIES_KEY = "authorities";

    public static final String CLIENT_ID = "client-id";

    public static final String AUTH_DELIMITER = ",";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ANONYMOUS = "anonymous";

    public static final String MFA_AUTHORITY = "MFA_REQUIRED";

    public static final String JSESSIONID = "JSESSIONID";

    public static final String AUTHENTICATION_EXCEPTION = "authenticationException";

    private ESakshamAuthConstant() {
        throw new AssertionError("No com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant instances for you!");
    }
}

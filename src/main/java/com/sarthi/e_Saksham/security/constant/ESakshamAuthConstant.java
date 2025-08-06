package com.sarthi.e_Saksham.security.constant;

public final class ESakshamAuthConstant {

    public static final String ISSUER = "e-Saksham";

    public static final String AUTHORITIES = "authorities";

    public static final String CLIENT_ID = "client-id";

    public static final String AUTH_DELIMITER = ",";

    public static final String ROLE_PREFIX = "ROLE_";

    private ESakshamAuthConstant() {
        throw new AssertionError("No com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant instances for you!");
    }
}

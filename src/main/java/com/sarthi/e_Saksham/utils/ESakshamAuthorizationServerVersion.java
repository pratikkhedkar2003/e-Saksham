package com.sarthi.e_Saksham.utils;

public final class ESakshamAuthorizationServerVersion {

    private static final int MAJOR = 1;

    private static final int MINOR = 0;

    private static final int PATCH = 1;

    public static final long SERIAL_VERSION_UID = getVersion().hashCode();

    public static String getVersion() {
        return MAJOR + "." + MINOR + "." + PATCH;
    }

    private ESakshamAuthorizationServerVersion() {
        throw new AssertionError("No com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion instances for you!");
    }
}

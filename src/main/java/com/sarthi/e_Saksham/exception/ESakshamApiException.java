package com.sarthi.e_Saksham.exception;

import com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class ESakshamApiException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    public ESakshamApiException() {
        super("An error occurred");
    }

    public ESakshamApiException(String message) {
        super(message);
    }
}

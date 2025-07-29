package com.sarthi.e_Saksham.exception;

public class ESakshamApiException extends RuntimeException {

    public ESakshamApiException() {
        super("An error occurred");
    }

    public ESakshamApiException(String message) {
        super(message);
    }
}

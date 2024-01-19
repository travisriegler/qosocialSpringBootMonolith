package com.qosocial.v1api.common.exception;

public class InvalidAuthenticationTypeException extends RuntimeException {

    public InvalidAuthenticationTypeException() {
        super("Authentication type is not supported");
    }

    public InvalidAuthenticationTypeException(String message) {
        super(message);
    }

    public InvalidAuthenticationTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationTypeException(Throwable cause) {
        super("Authentication type is not supported", cause);
    }
}

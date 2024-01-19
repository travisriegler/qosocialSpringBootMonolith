package com.qosocial.v1api.auth.exception;

public class GenericLogoutException extends RuntimeException {

    public GenericLogoutException() {
        super("Unexpected logout error");
    }

    public GenericLogoutException(String message) {
        super(message);
    }

    public GenericLogoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericLogoutException(Throwable cause) {
        super("Unexpected logout error", cause);
    }
}

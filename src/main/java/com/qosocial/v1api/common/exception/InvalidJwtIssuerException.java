package com.qosocial.v1api.common.exception;

public class InvalidJwtIssuerException extends RuntimeException {

    public InvalidJwtIssuerException() {
        super("Invalid jwt issuer");
    }

    public InvalidJwtIssuerException(String message) {
        super(message);
    }

    public InvalidJwtIssuerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtIssuerException(Throwable cause) {
        super("Invalid jwt issuer", cause);
    }
}

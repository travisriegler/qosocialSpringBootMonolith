package com.qosocial.v1api.common.exception;

public class JwtTokenMissingException extends RuntimeException {

    public JwtTokenMissingException() {
        super("Jwt Token is missing");
    }

    public JwtTokenMissingException(String message) {
        super(message);
    }

    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenMissingException(Throwable cause) {
        super("Jwt Token is missing", cause);
    }
}

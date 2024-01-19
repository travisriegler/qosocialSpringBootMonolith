package com.qosocial.v1api.auth.exception;

public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Unauthorized, refresh token is expired or invalid");
    }

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRefreshTokenException(Throwable cause) {
        super("Unauthorized, refresh token is expired or invalid", cause);
    }
}

package com.qosocial.v1api.auth.exception;

public class ExpiredRefreshTokenException extends RuntimeException {

    public ExpiredRefreshTokenException() {
        super("Unauthorized, expired refresh token");
    }

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

    public ExpiredRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredRefreshTokenException(Throwable cause) {
        super("Unauthorized, expired refresh token", cause);
    }
}

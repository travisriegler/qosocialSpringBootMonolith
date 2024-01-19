package com.qosocial.v1api.auth.exception;

public class NoRefreshTokenException extends RuntimeException {

    public NoRefreshTokenException() {
        super("No refresh token was provided");
    }

    public NoRefreshTokenException(String message) {
        super(message);
    }

    public NoRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRefreshTokenException(Throwable cause) {
        super("No refresh token was provided", cause);
    }
}

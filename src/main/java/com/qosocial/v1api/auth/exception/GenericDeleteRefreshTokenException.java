package com.qosocial.v1api.auth.exception;

public class GenericDeleteRefreshTokenException extends RuntimeException {

    public GenericDeleteRefreshTokenException() {
        super("Unexpected error when deleting the refresh token, please try again");
    }

    public GenericDeleteRefreshTokenException(String message) {
        super(message);
    }

    public GenericDeleteRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericDeleteRefreshTokenException(Throwable cause) {
        super("Unexpected error when deleting the refresh token, please try again", cause);
    }
}

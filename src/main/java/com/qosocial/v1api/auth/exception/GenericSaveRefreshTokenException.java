package com.qosocial.v1api.auth.exception;

public class GenericSaveRefreshTokenException extends RuntimeException {

    public GenericSaveRefreshTokenException() {
        super("Unexpected error when saving the refresh token, please try again");
    }

    public GenericSaveRefreshTokenException(String message) {
        super(message);
    }

    public GenericSaveRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericSaveRefreshTokenException(Throwable cause) {
        super("Unexpected error when saving the refresh token, please try again", cause);
    }
}

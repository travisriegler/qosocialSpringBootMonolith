package com.qosocial.v1api.common.exception;

public class InvalidImageException extends RuntimeException {

    public InvalidImageException() {
        super("The provided image was invalid, please try again");
    }

    public InvalidImageException(String message) {
        super(message);
    }

    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageException(Throwable cause) {
        super("The provided image was invalid, please try again", cause);
    }
}

package com.qosocial.v1api.auth.exception;

public class GenericLoginException extends RuntimeException {

    public GenericLoginException() {
        super("Unexpected login error, please try again");
    }

    public GenericLoginException(String message) {
        super(message);
    }

    public GenericLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericLoginException(Throwable cause) {
        super("Unexpected login error, please try again", cause);
    }
}

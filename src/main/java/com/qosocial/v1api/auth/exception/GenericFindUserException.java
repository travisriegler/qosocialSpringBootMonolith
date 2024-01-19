package com.qosocial.v1api.auth.exception;

public class GenericFindUserException extends RuntimeException {
    public GenericFindUserException() {
        super("Unexpected error when retrieving the user, please try again");
    }

    public GenericFindUserException(String message) {
        super(message);
    }

    public GenericFindUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericFindUserException(Throwable cause) {
        super("Unexpected error when retrieving the user, please try again", cause);
    }
}


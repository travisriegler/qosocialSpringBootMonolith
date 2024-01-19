package com.qosocial.v1api.profile.exception;

public class GenericCreateProfileException extends RuntimeException {

    public GenericCreateProfileException() {
        super("Unexpected error when creating the profile, please try again");
    }

    public GenericCreateProfileException(String message) {
        super(message);
    }

    public GenericCreateProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericCreateProfileException(Throwable cause) {
        super("Unexpected error when creating the profile, please try again", cause);
    }
}

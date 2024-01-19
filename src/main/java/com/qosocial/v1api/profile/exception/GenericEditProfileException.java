package com.qosocial.v1api.profile.exception;

public class GenericEditProfileException extends RuntimeException {

    public GenericEditProfileException() {
        super("Encountered an error updating your profile, please try again");
    }

    public GenericEditProfileException(String message) {
        super(message);
    }

    public GenericEditProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericEditProfileException(Throwable cause) {
        super("Encountered an error updating your profile, please try again", cause);
    }
}

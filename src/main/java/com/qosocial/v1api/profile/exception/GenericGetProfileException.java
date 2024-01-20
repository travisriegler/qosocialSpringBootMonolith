package com.qosocial.v1api.profile.exception;

public class GenericGetProfileException extends RuntimeException {

    public GenericGetProfileException() {
        super("Error retrieving the profile, please try again");
    }

    public GenericGetProfileException(String message) {
        super(message);
    }

    public GenericGetProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGetProfileException(Throwable cause) {
        super("Error retrieving the profile, please try again", cause);
    }
}

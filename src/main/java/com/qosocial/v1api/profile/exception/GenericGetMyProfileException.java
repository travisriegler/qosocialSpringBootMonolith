package com.qosocial.v1api.profile.exception;

public class GenericGetMyProfileException extends RuntimeException {

    public GenericGetMyProfileException() {
        super("Error retrieving your profile, please try again");
    }

    public GenericGetMyProfileException(String message) {
        super(message);
    }

    public GenericGetMyProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGetMyProfileException(Throwable cause) {
        super("Error retrieving your profile, please try again", cause);
    }
}

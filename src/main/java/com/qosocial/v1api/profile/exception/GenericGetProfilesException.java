package com.qosocial.v1api.profile.exception;

public class GenericGetProfilesException extends RuntimeException {

    public GenericGetProfilesException() {
        super("Error retrieving the profiles, please try again");
    }

    public GenericGetProfilesException(String message) {
        super(message);
    }

    public GenericGetProfilesException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGetProfilesException(Throwable cause) {
        super("Error retrieving the profiles, please try again", cause);
    }
}

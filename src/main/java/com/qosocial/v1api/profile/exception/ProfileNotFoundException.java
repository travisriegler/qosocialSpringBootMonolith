package com.qosocial.v1api.profile.exception;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException() {
        super("Unable to find the profile, please try again");
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileNotFoundException(Throwable cause) {
        super("Unable to find the profile, please try again", cause);
    }

}

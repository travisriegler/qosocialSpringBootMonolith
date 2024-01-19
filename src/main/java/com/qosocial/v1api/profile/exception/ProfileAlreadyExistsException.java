package com.qosocial.v1api.profile.exception;

public class ProfileAlreadyExistsException extends RuntimeException {

    public ProfileAlreadyExistsException() {
        super("You already have a profile, users are only allowed one profile");
    }

    public ProfileAlreadyExistsException(String message) {
        super(message);
    }

    public ProfileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileAlreadyExistsException(Throwable cause) {
        super("You already have a profile, users are only allowed one profile", cause);
    }
}

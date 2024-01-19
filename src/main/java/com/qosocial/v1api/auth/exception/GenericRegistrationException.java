package com.qosocial.v1api.auth.exception;

public class GenericRegistrationException extends RuntimeException {

    public GenericRegistrationException() {
        super("Unexpected error during registration, please try again");
    }

    public GenericRegistrationException(String message) {
        super(message);
    }

    public GenericRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericRegistrationException(Throwable cause) {
        super("Unexpected error during registration, please try again", cause);
    }
}


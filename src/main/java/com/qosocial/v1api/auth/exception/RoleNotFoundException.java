package com.qosocial.v1api.auth.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Unable to find the role, please try again");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFoundException(Throwable cause) {
        super("Unable to find the role, please try again", cause);
    }
}


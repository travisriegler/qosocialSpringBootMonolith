package com.qosocial.v1api.auth.exception;

public class GenericFindRoleException extends RuntimeException {
    public GenericFindRoleException() {
        super("Unexpected error when retrieving the role, please try again");
    }

    public GenericFindRoleException(String message) {
        super(message);
    }

    public GenericFindRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericFindRoleException(Throwable cause) {
        super("Unexpected error when retrieving the role, please try again", cause);
    }
}


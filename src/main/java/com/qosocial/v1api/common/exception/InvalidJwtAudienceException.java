package com.qosocial.v1api.common.exception;

public class InvalidJwtAudienceException extends RuntimeException {

    public InvalidJwtAudienceException() {
        super("Invalid jwt audience");
    }

    public InvalidJwtAudienceException(String message) {
        super(message);
    }

    public InvalidJwtAudienceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtAudienceException(Throwable cause) {
        super("Invalid jwt audience", cause);
    }
}

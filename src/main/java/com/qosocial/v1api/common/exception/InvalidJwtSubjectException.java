package com.qosocial.v1api.common.exception;

public class InvalidJwtSubjectException extends RuntimeException {

    public InvalidJwtSubjectException() {
        super("Invalid jwt subject");
    }

    public InvalidJwtSubjectException(String message) {
        super(message);
    }

    public InvalidJwtSubjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtSubjectException(Throwable cause) {
        super("Invalid jwt subject", cause);
    }
}

package com.qosocial.v1api.common.exception;

public class InvalidImageTypeException extends RuntimeException {

    public InvalidImageTypeException() {
        super("Invalid file type. Only image files are allowed.");
    }

    public InvalidImageTypeException(String message) {
        super(message);
    }

    public InvalidImageTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageTypeException(Throwable cause) {
        super("Invalid file type. Only image files are allowed.", cause);
    }
}

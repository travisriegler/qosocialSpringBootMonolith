package com.qosocial.v1api.common.exception;

public class GenericSaveImageException extends RuntimeException {

    public GenericSaveImageException() {
        super("Encountered an error saving your image, please try again");
    }

    public GenericSaveImageException(String message) {
        super(message);
    }

    public GenericSaveImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericSaveImageException(Throwable cause) {
        super("Encountered an error saving your image, please try again", cause);
    }
}

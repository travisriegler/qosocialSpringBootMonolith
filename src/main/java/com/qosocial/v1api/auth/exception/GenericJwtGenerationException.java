package com.qosocial.v1api.auth.exception;

public class GenericJwtGenerationException extends RuntimeException {

    public GenericJwtGenerationException() {
        super("Encountered an error generating the jwt, please try again");
    }

    public GenericJwtGenerationException(String message) {
        super(message);
    }

    public GenericJwtGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericJwtGenerationException(Throwable cause) {
        super("Encountered an error generating the jwt, please try again", cause);
    }
}

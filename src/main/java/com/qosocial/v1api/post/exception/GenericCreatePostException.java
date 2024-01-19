package com.qosocial.v1api.post.exception;

public class GenericCreatePostException extends RuntimeException {

    public GenericCreatePostException() {
        super("Encountered an error creating your post, please try again");
    }

    public GenericCreatePostException(String message) {
        super(message);
    }

    public GenericCreatePostException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericCreatePostException(Throwable cause) {
        super("Encountered an error creating your post, please try again", cause);
    }
}

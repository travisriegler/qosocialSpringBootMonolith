package com.qosocial.v1api.post.exception;

public class GenericUpdatePostByIdException extends RuntimeException {

    public GenericUpdatePostByIdException() {
        super("Encountered an error updating the post, please try again");
    }

    public GenericUpdatePostByIdException(String message) {
        super(message);
    }

    public GenericUpdatePostByIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericUpdatePostByIdException(Throwable cause) {
        super("Encountered an error updating the post, please try again", cause);
    }
}

package com.qosocial.v1api.post.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("Encountered an error finding the requested post, please try again");
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostNotFoundException(Throwable cause) {
        super("Encountered an error finding the requested post, please try again", cause);
    }
}

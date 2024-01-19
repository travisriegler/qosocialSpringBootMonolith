package com.qosocial.v1api.post.exception;

public class GenericGetPostsException extends RuntimeException {

    public GenericGetPostsException() {
        super("Encountered an error retrieving the posts, please try again");
    }

    public GenericGetPostsException(String message) {
        super(message);
    }

    public GenericGetPostsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGetPostsException(Throwable cause) {
        super("Encountered an error retrieving the posts, please try again", cause);
    }
}

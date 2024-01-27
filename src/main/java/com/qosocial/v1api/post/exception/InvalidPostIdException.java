package com.qosocial.v1api.post.exception;

public class InvalidPostIdException extends RuntimeException {

    public InvalidPostIdException() {
        super("Invalid post id. The post id must be a positive integer greater than zero.");
    }

    public InvalidPostIdException(String message) {
        super(message);
    }

    public InvalidPostIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPostIdException(Throwable cause) {
        super("Invalid post id. The post id must be a positive integer greater than zero.", cause);
    }
}

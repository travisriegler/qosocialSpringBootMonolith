package com.qosocial.v1api.post.exception;

public class UnauthorizedToModifyPostException extends RuntimeException {

    public UnauthorizedToModifyPostException() {
        super("You are not authorized to modify this post");
    }

    public UnauthorizedToModifyPostException(String message) {
        super(message);
    }

    public UnauthorizedToModifyPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedToModifyPostException(Throwable cause) {
        super("You are not authorized to modify this post", cause);
    }
}

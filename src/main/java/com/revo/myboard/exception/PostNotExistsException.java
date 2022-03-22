package com.revo.myboard.exception;

public class PostNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting post, probably post with id %d not exists in base!";

    public PostNotExistsException(long id) {
        super(MESSAGE.formatted(id));
    }
}

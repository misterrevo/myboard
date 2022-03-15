package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class CommentNotExistsException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Error while getting comment, probably comment with id %d not exists in base!";

    public CommentNotExistsException(long id) {
        super(EXCEPTION_MESSAGE.formatted(id));
    }

}

package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class PostNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting post, probably post with id %d not exists in base!";

    private long id;

    public PostNotExistsException(long id) {
        id = id;
    }

    @Override
    public String getMessage() {
        return MESSAGE.formatted(id);
    }

}

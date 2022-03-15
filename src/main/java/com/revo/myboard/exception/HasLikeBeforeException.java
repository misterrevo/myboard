package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class HasLikeBeforeException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Probably object of id %s has like from user!";

    public HasLikeBeforeException(long id) {
        super(EXCEPTION_MESSAGE.formatted(id));
    }

}

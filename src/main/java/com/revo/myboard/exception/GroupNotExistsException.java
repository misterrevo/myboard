package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class GroupNotExistsException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Error while getting group, probably group with id %d not exists in base!";

    public GroupNotExistsException(long id) {
        super(EXCEPTION_MESSAGE.formatted(id));
    }

}

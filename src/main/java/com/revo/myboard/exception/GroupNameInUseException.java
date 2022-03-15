package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class GroupNameInUseException extends RuntimeException {

    private static final String MESSAGE = "Error while naming group, probably group with name %s exists in base!";

    public GroupNameInUseException(String name) {
        super(MESSAGE.formatted(name));
    }

}

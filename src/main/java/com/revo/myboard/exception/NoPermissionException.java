package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class NoPermissionException extends RuntimeException {

    private static final String MESSAGE = "User %s try to do something without permissions to do that!";

    public NoPermissionException(String value) {
        super(MESSAGE.formatted(value));
    }

}

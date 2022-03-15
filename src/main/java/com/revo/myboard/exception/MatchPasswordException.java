package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class MatchPasswordException extends RuntimeException {

    private static final String MESSAGE = "Error while changing users password, old and new password is the same!";

    public MatchPasswordException(String value) {
        super(MESSAGE.formatted(value));
    }

}

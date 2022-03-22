package com.revo.myboard.exception;

public class MatchPasswordException extends RuntimeException {

    private static final String MESSAGE = "Error while changing users password to %s, old and new password is the same!";

    public MatchPasswordException(String value) {
        super(MESSAGE.formatted(value));
    }
}

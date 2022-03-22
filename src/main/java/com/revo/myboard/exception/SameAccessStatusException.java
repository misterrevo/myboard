package com.revo.myboard.exception;

public class SameAccessStatusException extends RuntimeException {

    private static final String MESSAGE = "Error while changing status by %s, probably %b is the same value like in method action!";

    public SameAccessStatusException(String value, boolean is) {
        super(MESSAGE.formatted(value, is));
    }
}
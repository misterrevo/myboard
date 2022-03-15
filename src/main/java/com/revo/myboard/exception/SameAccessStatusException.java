package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class SameAccessStatusException extends RuntimeException {

    private static final String MESSAGE = "Error while activating changing user access, probably %b is the same value like in method action!";

    public SameAccessStatusException(String value, boolean is) {
        super(MESSAGE.formatted(value, is));
    }

}
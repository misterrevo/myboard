package com.revo.myboard.exception;

public class UserIsActiveException extends RuntimeException {

    private static final String MESSAGE = "Error while activating user, probably user %s is alredy activated!";

    public UserIsActiveException(String value) {
        super(MESSAGE.formatted(value));
    }
}

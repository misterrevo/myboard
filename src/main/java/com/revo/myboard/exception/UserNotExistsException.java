package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class UserNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting user, probably user with details %s not exists in base!";

    public UserNotExistsException(String detail){
        super(MESSAGE.formatted(detail));
    }
}

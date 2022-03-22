package com.revo.myboard.exception;

import com.revo.myboard.user.User;

public class UserIsOwnerException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Error while reporting object with id %d because user with login %s is owner of this object!";

    public UserIsOwnerException(User user, long id) {
        super(EXCEPTION_MESSAGE.formatted(id, user.getLogin()));
    }
}

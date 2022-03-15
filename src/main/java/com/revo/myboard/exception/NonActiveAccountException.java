package com.revo.myboard.exception;

import org.springframework.security.core.AuthenticationException;

/*
 * Created By Revo
 */

public class NonActiveAccountException extends AuthenticationException {

    private static final String EXCEPTION_MESSAGE = "Account with login %s is not activated!";

    public NonActiveAccountException(String login) {
        super(EXCEPTION_MESSAGE.formatted(login));
    }

}

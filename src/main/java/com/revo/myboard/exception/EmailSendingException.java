package com.revo.myboard.exception;

public class EmailSendingException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Server has problems while sending email to %s, error message: %s";

    public EmailSendingException(String email, String message) {
        super(EXCEPTION_MESSAGE.formatted(email, message));
    }
}

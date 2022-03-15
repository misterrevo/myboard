package com.revo.myboard.exception;

public class PostNameInUseException extends RuntimeException {

    private static final String MESSAGE = "Error while naming category, probably category with name %s exists in base!";

    public PostNameInUseException(String name) {
        super(MESSAGE.formatted(name));
    }

}

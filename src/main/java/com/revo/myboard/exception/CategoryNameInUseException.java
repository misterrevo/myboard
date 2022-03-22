package com.revo.myboard.exception;

public class CategoryNameInUseException extends RuntimeException {

    private static final String MESSAGE = "Error while naming category, probably category with name %s exists in base!";

    public CategoryNameInUseException(String name) {
        super(MESSAGE.formatted(name));
    }
}

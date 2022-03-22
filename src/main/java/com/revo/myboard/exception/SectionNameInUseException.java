package com.revo.myboard.exception;

public class SectionNameInUseException extends RuntimeException {

    private static final String MESSAGE = "Error while naming section, probably section with name %s exists in base!";

    public SectionNameInUseException(String name) {
        super(MESSAGE.formatted(name));
    }
}

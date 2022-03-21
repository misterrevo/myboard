package com.revo.myboard.exception;

public class ImageNameInUseException extends RuntimeException {

    private static final String MESSAGE = "Image with name %s probably exists in base!";

    public ImageNameInUseException(String name) {
        super(MESSAGE.formatted(name));
    }
}

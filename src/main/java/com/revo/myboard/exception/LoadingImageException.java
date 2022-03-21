package com.revo.myboard.exception;

public class LoadingImageException extends RuntimeException {

    private static final String MESSAGE = "Error while loading image with name %s from resource";

    public LoadingImageException(String filename) {
        super(MESSAGE.formatted(filename));
    }
}

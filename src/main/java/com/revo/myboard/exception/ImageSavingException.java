package com.revo.myboard.exception;

public class ImageSavingException extends RuntimeException{

    private static final String MESSAGE = "Error while saving image named %s to resources file!";

    public ImageSavingException(String name) {
        super(MESSAGE.formatted(name));
    }
}

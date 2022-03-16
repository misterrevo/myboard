package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class ImageNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting image from repository, probably image with name %s not exists in base!";

    public ImageNotExistsException(String name) {
        super(MESSAGE.formatted(name));
    }
}

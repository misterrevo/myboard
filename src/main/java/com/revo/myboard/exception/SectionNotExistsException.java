package com.revo.myboard.exception;

public class SectionNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting section, probably section with id %d not exists in base!";

    public SectionNotExistsException(long id){
        super(MESSAGE.formatted(id));
    }
}
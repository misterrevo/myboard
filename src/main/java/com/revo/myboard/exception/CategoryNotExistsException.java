package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class CategoryNotExistsException extends RuntimeException {

    private static final String MESSAGE = "Error while getting category, probably category with id %d not exists in base!";

    public CategoryNotExistsException(long id){
        super(MESSAGE.formatted(id));
    }

}

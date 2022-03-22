package com.revo.myboard.exception;

public class GroupNotExistsException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_ID = "Error while getting group, probably group with id %d not exists in base!";
    private static final String EXCEPTION_MESSAGE_NAME = "Error while getting group, probably group with name %s not exists in base!";

    public GroupNotExistsException(long id) {
        super(EXCEPTION_MESSAGE_ID.formatted(id));
    }

    public GroupNotExistsException(String name) {
        super(EXCEPTION_MESSAGE_NAME.formatted(name));
    }
}

package com.revo.myboard.exception;

/*
 * Created By Revo
 */

public class ReportNotExistsException  extends RuntimeException {

    private static final String MESSAGE = "Error while getting report, probably report with id %d not exists in base!";

    public ReportNotExistsException(long id){
        super(MESSAGE.formatted(id));
    }

}

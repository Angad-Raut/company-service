package com.projectx.company_service.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String msg) {
        super(msg);
    }
}

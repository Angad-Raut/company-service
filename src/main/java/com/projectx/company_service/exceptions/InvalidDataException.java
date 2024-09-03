package com.projectx.company_service.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String msg) {
        super(msg);
    }
}

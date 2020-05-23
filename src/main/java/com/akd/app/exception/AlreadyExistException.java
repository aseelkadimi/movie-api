package com.akd.app.exception;

public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}

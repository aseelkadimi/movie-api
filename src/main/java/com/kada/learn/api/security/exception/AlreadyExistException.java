package com.kada.learn.api.security.exception;

public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}

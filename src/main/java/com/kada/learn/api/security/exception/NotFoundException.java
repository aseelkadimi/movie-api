package com.kada.learn.api.security.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

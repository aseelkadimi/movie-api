package com.kada.learn.api.security.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AlreadyExistException.class)
    protected ResponseEntity<Object> handleAlreadyExistException(RuntimeException ex, WebRequest request) {
        ApiError apiError = new ApiError(LocalDate.now().toString() , HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getServletPath());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
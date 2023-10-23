package com.wooreal.gravitygather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class EntityControllerAdvice {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<String> NoEntityException(final BusinessLogicException e) {
        return new ResponseEntity<>(e.getMessage(), e.getExceptionCode().getHttpStatus());
    }
}
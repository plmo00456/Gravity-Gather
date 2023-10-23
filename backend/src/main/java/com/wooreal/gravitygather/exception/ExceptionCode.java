package com.wooreal.gravitygather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ExceptionCode {

    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
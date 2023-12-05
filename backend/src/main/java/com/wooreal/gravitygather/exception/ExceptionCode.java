package com.wooreal.gravitygather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ExceptionCode {

    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String error;

    @Getter
    private String message;

    ExceptionCode(HttpStatus httpStatus, String error, String message) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
    }

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

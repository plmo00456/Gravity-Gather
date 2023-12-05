package com.wooreal.gravitygather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(HttpStatus httpStatus) {
        super("오류가 발생하였습니다. 관리자에게 문의해주세요.");
        this.exceptionCode = new ExceptionCode(httpStatus, httpStatus.name(), httpStatus.getReasonPhrase());
    }

    public BusinessLogicException(HttpStatus httpStatus, String error, String message) {
        super(message);
        this.exceptionCode = new ExceptionCode(httpStatus, error, message);
    }

    public BusinessLogicException(HttpStatus httpStatus, String message) {
        super(message);
        this.exceptionCode = new ExceptionCode(httpStatus, message);
    }
}

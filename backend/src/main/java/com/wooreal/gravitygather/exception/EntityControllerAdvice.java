package com.wooreal.gravitygather.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class EntityControllerAdvice {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<?> NoEntityException(final BusinessLogicException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        map.put("custom", true);
        return new ResponseEntity<>(map, e.getExceptionCode().getHttpStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> globalExceptionHandler(final Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

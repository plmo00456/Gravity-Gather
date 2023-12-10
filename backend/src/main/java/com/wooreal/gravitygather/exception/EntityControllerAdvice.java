package com.wooreal.gravitygather.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityControllerAdvice {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<?> NoEntityException(final BusinessLogicException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        map.put("custom", true);
        return new ResponseEntity<>(map, e.getExceptionCode().getHttpStatus());
    }
}
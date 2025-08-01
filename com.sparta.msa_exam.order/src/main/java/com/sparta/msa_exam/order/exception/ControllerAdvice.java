package com.sparta.msa_exam.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(CustomRuntimeException e) {
        return buildResponseEntity(e);
    }

    private ResponseEntity<String> buildResponseEntity(CustomRuntimeException e) {
        return ResponseEntity.status(e.getStatus())
                .body(e.getMessage());
    }
}

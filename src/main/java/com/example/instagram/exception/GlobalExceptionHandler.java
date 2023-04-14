package com.example.instagram.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleGeneralAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> objectBody = new LinkedHashMap<>();
        objectBody.put("timestamp", new Date());
        objectBody.put("error", ex.getMessage());
        objectBody.put("status", HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(objectBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, Object> objectBody = new LinkedHashMap<>();
        objectBody.put("timestamp", new Date());
        objectBody.put("error", ex.getMessage());
        objectBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(objectBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.example.social_network01.exception.handler;

import com.example.social_network01.exception.custom.InvalidMediaTypeException;
import com.example.social_network01.exception.custom.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException ex) {
        log.error("Storage error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "File storage error", "details", ex.getMessage()));
    }

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<?> handleInvalidMediaType(InvalidMediaTypeException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Invalid media type", "details", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(Map.of("error", "Internal server error"));
    }
}

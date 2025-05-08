package com.example.social_network01.exception.handler;

import com.example.social_network01.exception.custom.*;
import com.example.social_network01.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(StorageException ex) {
        log.error("Storage error: {}", ex.getMessage(), ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "File storage error",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMediaType(InvalidMediaTypeException ex) {
        log.warn("Invalid media type: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid media type",
                ex.getMessage()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "User not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("File I/O error: {}", ex.getMessage(), ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "File access error",
                ex.getMessage()
        );
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFound(PostNotFoundException ex) {
        log.warn("Post not found: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Post not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileType(InvalidFileTypeException ex) {
        log.warn("Invalid file type: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid file type",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotFound(MessageNotFoundException ex) {
        log.warn("Message not found: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Message not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChatNotFound(ChatNotFoundException ex) {
        log.warn("Chat not found: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Chat not found",
                ex.getMessage()
        );
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> handleForbidden(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String details
    ) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        error,
                        details
                ));
    }
}
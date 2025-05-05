package com.example.social_network01.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String details) {
        this.status = status;
        this.error = error;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
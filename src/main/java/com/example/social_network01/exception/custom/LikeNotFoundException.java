package com.example.social_network01.exception.custom;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(String message) {
        super(message);
    }

    public LikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
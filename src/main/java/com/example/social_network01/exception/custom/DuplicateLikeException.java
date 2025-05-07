package com.example.social_network01.exception.custom;

public class DuplicateLikeException extends RuntimeException {
    public DuplicateLikeException(String message) {
        super(message);
    }
}

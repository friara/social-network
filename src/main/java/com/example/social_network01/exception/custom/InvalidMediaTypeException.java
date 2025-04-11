package com.example.social_network01.exception.custom;

public class InvalidMediaTypeException extends RuntimeException {
    public InvalidMediaTypeException(String message) {
        super(message);
    }
}

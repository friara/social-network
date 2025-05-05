package com.example.social_network01.exception.custom;

// Непроверяемое исключение (наследуем от RuntimeException)
public class UserNotFoundException extends RuntimeException {
    // Конструктор с сообщением
    public UserNotFoundException(String message) {
        super(message);
    }

    // Дополнительно: конструктор с сообщением и причиной
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

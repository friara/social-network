package com.example.social_network01.exception.custom;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему посту.
 */
public class PostNotFoundException extends RuntimeException {

    /**
     * Создает исключение с сообщением об ошибке.
     * @param message Детализация ошибки (например, "Post with id 123 not found")
     */
    public PostNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     * @param message Детализация ошибки
     * @param cause Исходное исключение (если есть)
     */
    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
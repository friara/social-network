package com.example.social_network01.exception.custom;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему чату.
 */
public class ChatNotFoundException extends RuntimeException {

    /**
     * Создает исключение с сообщением об ошибке.
     * @param message Детализация ошибки (например, "Chat with id 123 not found")
     */
    public ChatNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     * @param message Детализация ошибки
     * @param cause Исходное исключение (если есть)
     */
    public ChatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
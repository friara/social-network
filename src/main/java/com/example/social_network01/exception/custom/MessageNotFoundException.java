package com.example.social_network01.exception.custom;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему сообщению.
 */
public class MessageNotFoundException extends RuntimeException {

    /**
     * Создает исключение с сообщением об ошибке.
     * @param message Детализация ошибки (например, "Message with id 123 not found")
     */
    public MessageNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     * @param message Детализация ошибки
     * @param cause Исходное исключение (если есть)
     */
    public MessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

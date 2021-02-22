package com.skb.testtask.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Список кодов исключений для фронта
 * В зависимости от договоренности между фронтом и бэком можно ориентироваться как на статусы (200-500 и т.п.)
 * или на текстовую кодировку исключения.
 * Я считаю что надежнее текст, т.к числа с 400 по 399 - это всго лишь 100 варинатов исключений, и когда числа закончатся
 * свои кастомные исколючения нужно будет подгонять под одну из уже сущетсвующих.
 * Текстовки типа USER_EXIST можно генерировать бесконечно.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    DEFAULT_ERROR_CODE("Произошла непредвиденная ошибка", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXIST("Пользователь уже существует", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("Пользователь не найден", HttpStatus.BAD_REQUEST),
    QUEUE_EXCEPTION("Ошибка работы с очередью", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_EXCEPTION("Ошибка отправки email", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
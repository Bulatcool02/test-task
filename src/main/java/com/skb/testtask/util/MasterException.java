package com.skb.testtask.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Кастомный класс исключений
 */
@Getter
public class MasterException extends RuntimeException {
    private final Date timestamp = new Date();
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public MasterException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }
}

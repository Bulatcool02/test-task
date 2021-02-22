package com.skb.testtask.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Тело исключения для возврата на фронт
 */
@Setter
@Getter
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private ErrorCode errorCode;
    private String message;

    public ErrorResponse(String message){
        this.timestamp = new Date();
        this.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = message;
    }
    public ErrorResponse(String message, HttpStatus httpStatus){
        this.timestamp = new Date();
        this.error = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.message = message;
    }
}
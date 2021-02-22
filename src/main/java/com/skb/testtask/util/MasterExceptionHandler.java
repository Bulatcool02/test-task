package com.skb.testtask.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Обычный хэндлер перехвата любых исключений.
 */
@ControllerAdvice
@Slf4j
public class MasterExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MasterException.class)
    public ResponseEntity<Object> handleException(MasterException ex){
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getHttpStatus());
        errorResponse.setErrorCode(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex){
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        errorResponse.setErrorCode(ErrorCode.DEFAULT_ERROR_CODE);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException{
    public BusinessException(String message) {
        super(HttpStatus.UNPROCESSABLE_CONTENT, message);
    }
}

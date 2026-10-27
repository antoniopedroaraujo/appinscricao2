package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends ApiException{
    public TokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

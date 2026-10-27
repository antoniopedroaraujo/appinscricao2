package com.aula.pos.appinscricao.handler;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestHandlerException {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(build(ex.getStatus(), ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> new ErrorResponse.FieldErrorResponse(e.getField(), e.getDefaultMessage())).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(build(HttpStatus.BAD_REQUEST, ex.getMessage(), errors));
    }

    private ErrorResponse build(HttpStatus status, String mensagem, List<ErrorResponse.FieldErrorResponse> errors) {
        return new ErrorResponse(LocalDateTime.now(), status, mensagem, errors);
    }
}

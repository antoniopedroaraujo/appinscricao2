package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> tratarApiException(
            ApiException ex) {

        Map<String, Object> resposta = Map.of(
                "status", ex.getStatus().value(),
                "erro", ex.getStatus().getReasonPhrase(),
                "mensagem", ex.getMessage(),
                "dataHora", LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroGeral(
            Exception ex) {

        Map<String, Object> resposta = Map.of(
                "status", 500,
                "erro", "Erro interno do servidor",
                "mensagem", "Ocorreu um erro inesperado.",
                "dataHora", LocalDateTime.now().toString()
        );

        return ResponseEntity.internalServerError().body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErroValidacao(
            MethodArgumentNotValidException ex) {

        Map<String, String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        erro -> erro.getField(),
                        erro -> erro.getDefaultMessage() != null
                                ? erro.getDefaultMessage()
                                : "Valor inválido",
                        (mensagemExistente, novaMensagem) -> mensagemExistente
                ));

        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "erro", "Erro de validação",
                "mensagens", erros,
                "dataHora", LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }
}
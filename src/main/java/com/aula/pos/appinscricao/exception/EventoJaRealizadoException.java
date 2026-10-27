package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class EventoJaRealizadoException extends ApiException {

    public EventoJaRealizadoException() {
        super(HttpStatus.BAD_REQUEST, "Não é possível excluir um evento já realizado");
    }
}

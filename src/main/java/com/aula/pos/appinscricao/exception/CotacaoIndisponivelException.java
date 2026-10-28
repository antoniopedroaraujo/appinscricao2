
package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class CotacaoIndisponivelException extends ApiException {

    public CotacaoIndisponivelException(String mensagem) {
        super(HttpStatus.SERVICE_UNAVAILABLE, mensagem);
    }
}

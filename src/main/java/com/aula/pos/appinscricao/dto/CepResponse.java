package com.aula.pos.appinscricao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CepResponse(
        String cep,
        String localidade,
        @JsonProperty("uf") String estado,
        String error
) {
}

package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InscricaoRequest(
        @NotNull(message = "Id da Pessoa Obrigatório")
        UUID pessoaId,
        @NotNull(message = "Id do Evento Obrigatório")
        UUID eventoId
) {
}

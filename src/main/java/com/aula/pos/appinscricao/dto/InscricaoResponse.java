package com.aula.pos.appinscricao.dto;

import com.aula.pos.appinscricao.enums.StatusInscricao;

import java.time.LocalDateTime;
import java.util.UUID;

public record InscricaoResponse(
        UUID id,
        PessoaResponse pessoa,
        EventoResponse evento,
        StatusInscricao statusInscricao,
        LocalDateTime dataHoraInscricao
) {
}

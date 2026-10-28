
package com.aula.pos.appinscricao.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record LimiteCompraResponse(
        UUID clienteId,
        YearMonth mesReferencia,
        BigDecimal limiteMensal,
        BigDecimal totalUtilizado,
        BigDecimal limiteDisponivel
) {}

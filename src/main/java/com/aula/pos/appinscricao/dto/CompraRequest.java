
package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CompraRequest(

        @NotNull(message = "O ID do cliente é obrigatório")
        UUID clienteId,

        @NotNull(message = "O valor em dólar é obrigatório")
        @DecimalMin(
                value = "0.00",
                inclusive = false,
                message = "O valor em dólar deve ser maior que zero"
        )
        BigDecimal valorUSD

) {}

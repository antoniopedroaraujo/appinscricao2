
package com.aula.pos.appinscricao.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompraCalculo {

    private static final BigDecimal TAXA_MENOR_100 =
            new BigDecimal("0.05");

    private static final BigDecimal TAXA_A_PARTIR_100 =
            new BigDecimal("0.02");

    private static final BigDecimal TRIBUTO =
            new BigDecimal("0.10");

    public static BigDecimal calcularValorConvertido(
            BigDecimal valorUSD, BigDecimal cotacao) {
        return valorUSD.multiply(cotacao)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularTaxaServico(
            BigDecimal valorUSD, BigDecimal valorConvertido) {

        BigDecimal percentual = valorUSD.compareTo(
                new BigDecimal("100")) < 0
                ? TAXA_MENOR_100
                : TAXA_A_PARTIR_100;

        return valorConvertido.multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularTributo(
            BigDecimal valorConvertido) {
        return valorConvertido.multiply(TRIBUTO)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularValorFinal(
            BigDecimal valorConvertido,
            BigDecimal taxaServico,
            BigDecimal tributo) {
        return valorConvertido.add(taxaServico).add(tributo)
                .setScale(2, RoundingMode.HALF_UP);
    }
}

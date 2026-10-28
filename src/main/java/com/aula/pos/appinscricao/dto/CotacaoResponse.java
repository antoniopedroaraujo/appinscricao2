
package com.aula.pos.appinscricao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CotacaoResponse {

    @JsonProperty("USDBRL")
    private UsdBrl usdBrl;

    @Getter
    @Setter
    public static class UsdBrl {

        private BigDecimal ask;

        @JsonProperty("create_date")
        private String createDate;
    }
}

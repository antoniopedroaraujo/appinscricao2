
package com.aula.pos.appinscricao.client;

import com.aula.pos.appinscricao.dto.CotacaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "awesomeApiClient",
        url = "https://economia.awesomeapi.com.br"
)
public interface AwesomeApiClient {

    @GetMapping("/json/last/USD-BRL")
    CotacaoResponse consultarCotacaoDolar();
}

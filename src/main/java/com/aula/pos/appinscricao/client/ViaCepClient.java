package com.aula.pos.appinscricao.client;

import com.aula.pos.appinscricao.dto.CepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name="viaCepClient",
        url="${integrations.viacep.url}"
)
public interface ViaCepClient {
    @GetMapping("/ws/{cep}/json")
    CepResponse buscarCep(@PathVariable String cep);
}

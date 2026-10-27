package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.client.ViaCepClient;
import com.aula.pos.appinscricao.dto.CepResponse;
import com.aula.pos.appinscricao.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepServiceImpl {

    private final ViaCepClient viaCepClient;

    public CepResponse buscarCep(String cep) {

        //58071-350
        var cepNormalizado = cep.replaceAll("\\D", "");

        if(cepNormalizado.length() != 8){
            throw new BusinessException("Cep Inválido");
        }

        CepResponse response = viaCepClient.buscarCep(cepNormalizado);

        if(response.error() != null && response.error().equalsIgnoreCase("true")){
            throw new BusinessException("Cep não encontrado");
        }

        return response;
    }
}

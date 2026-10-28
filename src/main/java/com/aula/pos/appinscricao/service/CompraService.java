
package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.dto.CompraRequest;
import com.aula.pos.appinscricao.dto.CompraResponse;
import com.aula.pos.appinscricao.dto.LimiteCompraResponse;

import java.util.List;
import java.util.UUID;

public interface CompraService {

    CompraResponse simular(CompraRequest request);

    CompraResponse comprar(CompraRequest request);

    List<CompraResponse> listarPorCliente(UUID clienteId);

    LimiteCompraResponse consultarLimite(UUID clienteId);
}

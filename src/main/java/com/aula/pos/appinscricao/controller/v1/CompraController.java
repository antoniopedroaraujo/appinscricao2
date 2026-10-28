
package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.CompraRequest;
import com.aula.pos.appinscricao.dto.CompraResponse;
import com.aula.pos.appinscricao.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/compras/simular")
    public CompraResponse simular(
            @Valid @RequestBody CompraRequest request) {
        return compraService.simular(request);
    }

    @PostMapping("/compras")
    @ResponseStatus(HttpStatus.CREATED)
    public CompraResponse comprar(
            @Valid @RequestBody CompraRequest request) {
        return compraService.comprar(request);
    }

    @GetMapping("/clientes/{id}/compras")
    public List<CompraResponse> listarPorCliente(
            @PathVariable("id") UUID clienteId) {
        return compraService.listarPorCliente(clienteId);
    }

    @GetMapping("/clientes/{id}/limite")
    public com.aula.pos.appinscricao.dto.LimiteCompraResponse consultarLimite(
            @PathVariable("id") UUID clienteId) {
        return compraService.consultarLimite(clienteId);
    }

}

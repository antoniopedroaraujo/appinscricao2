package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.InscricaoRequest;
import com.aula.pos.appinscricao.dto.InscricaoResponse;
import com.aula.pos.appinscricao.repository.InscricaoRepository;
import com.aula.pos.appinscricao.service.impl.InscricaoServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inscricoes")
@RequiredArgsConstructor
public class InscricaoController {

    private  final InscricaoServiceImpl inscricaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscricaoResponse criar(@Valid @RequestBody InscricaoRequest inscricaoRequest){
        return inscricaoService.criar(inscricaoRequest);
    }
}

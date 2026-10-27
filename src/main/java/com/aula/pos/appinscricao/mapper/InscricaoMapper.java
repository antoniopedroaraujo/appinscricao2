package com.aula.pos.appinscricao.mapper;

import com.aula.pos.appinscricao.dto.InscricaoResponse;
import com.aula.pos.appinscricao.model.Inscricao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InscricaoMapper {

    private final PessoaMapper pessoaMapper;
    private final EventoMapper eventoMapper;

    public InscricaoResponse toResponse(Inscricao inscricao){
        return new InscricaoResponse(
                inscricao.getId(),
                pessoaMapper.toResponse(inscricao.getPessoa()),
                eventoMapper.toResponse(inscricao.getEvento()),
                inscricao.getStatusInscricao(),
                inscricao.getDataHoraInscricao()
        );
    }
}

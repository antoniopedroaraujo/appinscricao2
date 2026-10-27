package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.InscricaoRequest;
import com.aula.pos.appinscricao.dto.InscricaoResponse;
import com.aula.pos.appinscricao.enums.StatusInscricao;
import com.aula.pos.appinscricao.exception.BusinessException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.InscricaoMapper;
import com.aula.pos.appinscricao.model.Inscricao;
import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.repository.InscricaoRepository;
import com.aula.pos.appinscricao.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InscricaoServiceImpl {

    private final InscricaoRepository inscricaoRepository;
    private final InscricaoMapper inscricaoMapper;
    private final PessoaRepository  pessoaRepository;
    private final EventoRepository eventoRepository;

    @Transactional
    public InscricaoResponse criar(InscricaoRequest request){
        //verificar se a pessoa existe
        var pessoa = pessoaRepository.findById(request.pessoaId())
                .orElseThrow(() -> new ResourceNotFoundException(request.pessoaId()));
        //verificar se o evento existe
        var evento = eventoRepository.findById(request.eventoId())
                .orElseThrow(() -> new ResourceNotFoundException(request.pessoaId()));
        //verificar se o evento já expirou
        if(evento.getDataHoraEvento().isBefore(LocalDateTime.now())){
            throw new BusinessException("Inscrições encerradas");
        }
        //verificar se a pessoa já está inscrita
        if(inscricaoRepository.existsByPessoaIdAndEventoIdAndStatusInscricao(
                request.pessoaId(),
                request.eventoId(),
                StatusInscricao.ATIVA
        )){
            throw new BusinessException("Pessoa já está inscrita");
        }
        //verificar se o evento possui vagas disponives
        long qtdeInscricoes = inscricaoRepository.countByEventoIdAndStatusInscricao(
                request.eventoId(),
                StatusInscricao.ATIVA);
        if(qtdeInscricoes >= evento.getQtde()){
            throw new BusinessException("Evento não possui mais vaga");
        }
        //criar a inscricao
        var inscricao = new Inscricao(null, pessoa, evento, StatusInscricao.ATIVA, LocalDateTime.now());
        //salvo e retorno a inscricao pelo mapper
        return inscricaoMapper.toResponse(inscricaoRepository.save(inscricao));
    }
}

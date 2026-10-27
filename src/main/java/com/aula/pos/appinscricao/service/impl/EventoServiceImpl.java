package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.BusinessException;
import com.aula.pos.appinscricao.exception.EventoDuplicadoException;
import com.aula.pos.appinscricao.exception.EventoJaRealizadoException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.EventoMapper;
import com.aula.pos.appinscricao.model.Evento;
import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final CepServiceImpl cepService;

    @Override
    public Page<EventoResponse> findAll(
            String nome,
            String cidade,
            String estado,
            Pageable pageable
    ) {
        Page<Evento> eventos;

        if (hasText(nome)) {
            eventos = eventoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (hasText(cidade)) {
            eventos = eventoRepository.findByCidadeIgnoreCase(cidade, pageable);
        } else if (hasText(estado)) {
            eventos = eventoRepository.findByEstadoIgnoreCase(estado, pageable);
        } else {
            eventos = eventoRepository.findAll(pageable);
        }

        return eventos.map(eventoMapper::toResponse);
    }

    @Override
    public EventoResponse findById(UUID id) {
        return eventoMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public EventoResponse save(EventoRequest request) {
        validateDuplicate(request, null);
        Evento evento = eventoMapper.toEntity(request);
        validarEndereco(request);
        buscarService(request.cep(), evento);
        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public EventoResponse update(UUID id, EventoRequest request) {
        Evento evento = findByIdOrThrow(id);
        validateDuplicate(request, id);
        eventoMapper.updateEntity(request, evento);
        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Evento evento = findByIdOrThrow(id);
        if (evento.getDataHoraEvento().isBefore(LocalDateTime.now())) {
            throw new EventoJaRealizadoException();
        }
        eventoRepository.delete(evento);
    }

    private Evento findByIdOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private void validateDuplicate(EventoRequest request, UUID ignoredId) {
        boolean duplicate = ignoredId == null
                ? eventoRepository.existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCase(
                        request.nome(), request.dataHoraEvento(), request.cidade())
                : eventoRepository.existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(
                        request.nome(), request.dataHoraEvento(), request.cidade(), ignoredId);

        if (duplicate) {
            throw new EventoDuplicadoException();
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void buscarService(String cep, Evento evento){
        if(cep != null && !cep.isBlank()){
            var response = cepService.buscarCep(cep);
            evento.setCidade(response.localidade());
            evento.setEstado(response.estado());
        }
    }

    private void validarEndereco(EventoRequest request){
        var cepAusente = !hasText(request.cep());
        var cidadeAusente = !hasText(request.cidade());
        var estadoAusente = !hasText(request.estado());

        if(cepAusente && (cidadeAusente || estadoAusente)){
            throw new BusinessException("Cep ausente, cidade e estado não foram eviados");
        }
    }
}

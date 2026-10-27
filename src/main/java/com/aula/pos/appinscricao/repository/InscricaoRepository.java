package com.aula.pos.appinscricao.repository;

import com.aula.pos.appinscricao.enums.StatusInscricao;
import com.aula.pos.appinscricao.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, UUID> {

    boolean existsByPessoaIdAndEventoIdAndStatusInscricao(UUID pessoaId, UUID eventoId, StatusInscricao status);
    long countByEventoIdAndStatusInscricao(UUID eventoId, StatusInscricao status);
}

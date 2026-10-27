package com.aula.pos.appinscricao.model;

import com.aula.pos.appinscricao.enums.StatusInscricao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tb_inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="pessoa_id", nullable = false)
    private Pessoa pessoa;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="evento_id", nullable = false)
    private Evento evento;
    @Enumerated(EnumType.STRING)
    private StatusInscricao statusInscricao;
    @Column(nullable = false)
    private LocalDateTime dataHoraInscricao;
}


package com.aula.pos.appinscricao.repository;

import com.aula.pos.appinscricao.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, UUID> {

    List<Compra> findByCliente_IdOrderByDataCompraDesc(UUID clienteId);

    @Query("""
        SELECT COALESCE(SUM(c.valorFinalBRL), 0)
        FROM Compra c
        WHERE c.cliente.id = :clienteId
          AND c.dataCompra >= :inicio
          AND c.dataCompra < :fim
        """)
    BigDecimal somarComprasDoPeriodo(
            @Param("clienteId") UUID clienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}

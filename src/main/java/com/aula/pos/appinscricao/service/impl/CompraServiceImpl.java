
package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.client.AwesomeApiClient;
import com.aula.pos.appinscricao.dto.CompraRequest;
import com.aula.pos.appinscricao.dto.CompraResponse;
import com.aula.pos.appinscricao.dto.CotacaoResponse;
import com.aula.pos.appinscricao.dto.LimiteCompraResponse;
import com.aula.pos.appinscricao.exception.BusinessException;
import com.aula.pos.appinscricao.exception.CotacaoIndisponivelException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.model.Cliente;
import com.aula.pos.appinscricao.model.Compra;
import com.aula.pos.appinscricao.repository.ClienteRepository;
import com.aula.pos.appinscricao.repository.CompraRepository;
import com.aula.pos.appinscricao.service.CompraService;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class CompraServiceImpl implements CompraService {

    private static final BigDecimal LIMITE_MENSAL =
            new BigDecimal("10000.00");

    private final CompraRepository compraRepository;
    private final ClienteRepository clienteRepository;
    private final AwesomeApiClient awesomeApiClient;

    public CompraServiceImpl(
            CompraRepository compraRepository,
            ClienteRepository clienteRepository,
            AwesomeApiClient awesomeApiClient) {
        this.compraRepository = compraRepository;
        this.clienteRepository = clienteRepository;
        this.awesomeApiClient = awesomeApiClient;
    }

    private Cliente buscarCliente(UUID clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(clienteId));
    }

    private CotacaoResponse.UsdBrl consultarCotacao() {
        CotacaoResponse resposta;

        try {
            resposta = awesomeApiClient.consultarCotacaoDolar();
        } catch (FeignException e) {
            throw new CotacaoIndisponivelException(
                    "Não foi possível consultar a cotação do dólar");
        } catch (RuntimeException e) {
            throw new CotacaoIndisponivelException(
                    "Erro ao acessar o serviço de cotação do dólar");
        }

        if (resposta == null
                || resposta.getUsdBrl() == null
                || resposta.getUsdBrl().getAsk() == null
                || resposta.getUsdBrl().getCreateDate() == null
                || resposta.getUsdBrl().getCreateDate().isBlank()) {
            throw new BusinessException(
                    "A resposta da cotação está incompleta");
        }

        if (resposta.getUsdBrl().getAsk()
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Cotação do dólar inválida");
        }

        return resposta.getUsdBrl();
    }

    private LocalDateTime interpretarDataCotacao(String data) {
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            return LocalDateTime.parse(data, formato);
        } catch (DateTimeParseException e) {
            throw new BusinessException(
                    "A data da cotação retornada pela API é inválida");
        }
    }

    private CompraResponse calcular(CompraRequest request) {
        if (request == null
                || request.clienteId() == null
                || request.valorUSD() == null
                || request.valorUSD().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "Informe um cliente e um valor em dólar maior que zero");
        }

        Cliente cliente = buscarCliente(request.clienteId());

        CotacaoResponse.UsdBrl cotacaoResposta = consultarCotacao();
        BigDecimal cotacao = cotacaoResposta.getAsk();

        LocalDateTime dataCotacao =
                interpretarDataCotacao(cotacaoResposta.getCreateDate());

        BigDecimal convertido =
                CompraCalculo.calcularValorConvertido(
                        request.valorUSD(), cotacao);

        BigDecimal taxa =
                CompraCalculo.calcularTaxaServico(
                        request.valorUSD(), convertido);

        BigDecimal tributo =
                CompraCalculo.calcularTributo(convertido);

        BigDecimal valorFinal =
                CompraCalculo.calcularValorFinal(
                        convertido, taxa, tributo);

        return new CompraResponse(
                null,
                cliente.getId(),
                request.valorUSD(),
                cotacao,
                convertido,
                taxa,
                tributo,
                valorFinal,
                dataCotacao.toLocalDate(),
                LocalDateTime.now()
        );
    }

    private void validarLimiteMensal(
            UUID clienteId, BigDecimal valorNovaCompra) {

        YearMonth mesAtual = YearMonth.now();

        LocalDateTime inicio =
                mesAtual.atDay(1).atStartOfDay();

        LocalDateTime fim =
                mesAtual.plusMonths(1).atDay(1).atStartOfDay();

        BigDecimal totalAtual =
                compraRepository.somarComprasDoPeriodo(
                        clienteId, inicio, fim);

        if (totalAtual == null) {
            totalAtual = BigDecimal.ZERO;
        }

        BigDecimal totalComNovaCompra =
                totalAtual.add(valorNovaCompra);

        if (totalComNovaCompra.compareTo(LIMITE_MENSAL) > 0) {
            throw new BusinessException(
                    "A compra ultrapassa o limite mensal de R$ 10.000,00");
        }
    }

    @Override
    public CompraResponse simular(CompraRequest request) {
        return calcular(request);
    }

    @Override
    @Transactional
    public CompraResponse comprar(CompraRequest request) {
        CompraResponse calculo = calcular(request);

        validarLimiteMensal(
                request.clienteId(),
                calculo.valorFinalBRL());

        Cliente cliente = buscarCliente(request.clienteId());

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setValorUSD(calculo.valorUSD());
        compra.setCotacaoUtilizada(calculo.cotacaoUtilizada());
        compra.setValorConvertido(calculo.valorConvertido());
        compra.setTaxaServico(calculo.taxaServico());
        compra.setTributoSimulado(calculo.tributoSimulado());
        compra.setValorFinalBRL(calculo.valorFinalBRL());
        compra.setDataCotacao(calculo.dataCotacao());
        compra.setDataCompra(calculo.dataCompra());

        Compra salva = compraRepository.save(compra);

        return converterParaResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponse> listarPorCliente(UUID clienteId) {
        buscarCliente(clienteId);

        return compraRepository
                .findByCliente_IdOrderByDataCompraDesc(clienteId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LimiteCompraResponse consultarLimite(UUID clienteId) {
        buscarCliente(clienteId);

        YearMonth mesAtual = YearMonth.now();

        LocalDateTime inicio =
                mesAtual.atDay(1).atStartOfDay();

        LocalDateTime fim =
                mesAtual.plusMonths(1).atDay(1).atStartOfDay();

        BigDecimal totalUtilizado =
                compraRepository.somarComprasDoPeriodo(
                        clienteId, inicio, fim);

        if (totalUtilizado == null) {
            totalUtilizado = BigDecimal.ZERO;
        }

        BigDecimal limiteDisponivel =
                LIMITE_MENSAL.subtract(totalUtilizado);

        if (limiteDisponivel.compareTo(BigDecimal.ZERO) < 0) {
            limiteDisponivel = BigDecimal.ZERO;
        }

        return new LimiteCompraResponse(
                clienteId,
                mesAtual,
                LIMITE_MENSAL,
                totalUtilizado,
                limiteDisponivel
        );
    }

    private CompraResponse converterParaResponse(Compra compra) {
        return new CompraResponse(
                compra.getId(),
                compra.getCliente().getId(),
                compra.getValorUSD(),
                compra.getCotacaoUtilizada(),
                compra.getValorConvertido(),
                compra.getTaxaServico(),
                compra.getTributoSimulado(),
                compra.getValorFinalBRL(),
                compra.getDataCotacao(),
                compra.getDataCompra()
        );
    }
}

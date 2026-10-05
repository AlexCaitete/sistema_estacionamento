package com.estacionamento.service;

import com.estacionamento.model.Permanencia;
import com.estacionamento.model.TipoVeiculo;
import com.estacionamento.model.Vaga;
import com.estacionamento.model.Veiculo;
import com.estacionamento.repository.PermanenciaRepository;
import com.estacionamento.repository.VagaRepository;
import com.estacionamento.repository.VeiculoRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermanenciaService {

    private static final BigDecimal TARIFA_CARRO =
            new BigDecimal("10.00");

    private static final BigDecimal TARIFA_MOTO =
            new BigDecimal("5.00");

    private final PermanenciaRepository permanenciaRepository;
    private final VagaRepository vagaRepository;
    private final VeiculoRepository veiculoRepository;

    public PermanenciaService(
            PermanenciaRepository permanenciaRepository,
            VagaRepository vagaRepository,
            VeiculoRepository veiculoRepository) {

        this.permanenciaRepository = permanenciaRepository;
        this.vagaRepository = vagaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Permanencia registrarEntrada(Long idVeiculo, Long idVaga) {

        Veiculo veiculo = veiculoRepository.buscarPorId(idVeiculo);

        if (veiculo == null) {
            throw new IllegalArgumentException(
                    "Veículo não encontrado com o ID: " + idVeiculo
            );
        }

        Vaga vaga = vagaRepository.buscarPorId(idVaga);

        if (vaga == null) {
            throw new IllegalArgumentException(
                    "Vaga não encontrada com o ID: " + idVaga
            );
        }

        if (!vaga.isDisponivel()) {
            throw new IllegalStateException(
                    "A vaga " + vaga.getNumero() + " já está ocupada."
            );
        }

        if (vaga.getTipoVeiculo() != veiculo.getTipo()) {
            throw new IllegalStateException(
                    "A vaga selecionada não aceita veículo do tipo "
                            + veiculo.getTipo() + "."
            );
        }

        boolean veiculoJaEstaEstacionado =
                permanenciaRepository.buscarAbertas()
                        .stream()
                        .anyMatch(permanencia ->
                                permanencia.getVeiculo()
                                        .getPlaca()
                                        .equalsIgnoreCase(
                                                veiculo.getPlaca()
                                        )
                        );

        if (veiculoJaEstaEstacionado) {
            throw new IllegalStateException(
                    "Este veículo já possui uma permanência aberta."
            );
        }

        vaga.setDisponivel(false);

        Long novoId = gerarProximoId();

        Permanencia permanencia =
                new Permanencia(novoId, vaga, veiculo);

        permanenciaRepository.registrar(permanencia);

        return permanencia;
    }

    public Permanencia registrarSaida(Long id) {

        Permanencia permanencia = buscarPorId(id);

        if (permanencia.getSaida() != null) {
            throw new IllegalStateException(
                    "A saída desta permanência já foi registrada."
            );
        }

        LocalDateTime horarioSaida = LocalDateTime.now();

        BigDecimal valorCalculado = calcularValor(
                permanencia.getEntrada(),
                horarioSaida,
                permanencia.getVeiculo().getTipo()
        );

        permanencia.registrarSaida(valorCalculado.doubleValue());

        permanencia.getVaga().setDisponivel(true);

        return permanencia;
    }

    public BigDecimal consultarValor(Long id) {

        Permanencia permanencia = buscarPorId(id);

        if (permanencia.getValorPago() == null) {
            throw new IllegalStateException(
                    "A permanência ainda está aberta. " +
                    "O valor será calculado no momento da saída."
            );
        }

        return BigDecimal.valueOf(
                permanencia.getValorPago()
        ).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Permanencia> listar() {
        return permanenciaRepository.listarTodas();
    }

    public Permanencia buscarPorId(Long id) {

        return permanenciaRepository.listarTodas()
                .stream()
                .filter(permanencia ->
                        permanencia.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Permanência não encontrada com o ID: "
                                        + id
                        )
                );
    }

    public BigDecimal calcularFaturamento(
            LocalDateTime inicio,
            LocalDateTime fim) {

        if (inicio == null || fim == null) {
            throw new IllegalArgumentException(
                    "A data inicial e a data final são obrigatórias."
            );
        }

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException(
                    "A data final não pode ser anterior à data inicial."
            );
        }

        return permanenciaRepository.listarTodas()
                .stream()
                .filter(permanencia ->
                        permanencia.getSaida() != null)
                .filter(permanencia ->
                        permanencia.getValorPago() != null)
                .filter(permanencia ->
                        !permanencia.getSaida().isBefore(inicio)
                                && !permanencia.getSaida().isAfter(fim))
                .map(permanencia ->
                        BigDecimal.valueOf(
                                permanencia.getValorPago()
                        ))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularFaturamentoDoDia(
            LocalDate data) {

        if (data == null) {
            throw new IllegalArgumentException(
                    "A data é obrigatória."
            );
        }

        LocalDateTime inicio =
                data.atStartOfDay();

        LocalDateTime fim =
                data.plusDays(1).atStartOfDay().minusNanos(1);

        return calcularFaturamento(inicio, fim);
    }

    private BigDecimal calcularValor(
            LocalDateTime entrada,
            LocalDateTime saida,
            TipoVeiculo tipoVeiculo) {

        if (entrada == null || saida == null) {
            throw new IllegalArgumentException(
                    "Os horários de entrada e saída são obrigatórios."
            );
        }

        if (saida.isBefore(entrada)) {
            throw new IllegalArgumentException(
                    "O horário de saída não pode ser anterior " +
                    "ao horário de entrada."
            );
        }

        long minutos =
                Duration.between(entrada, saida).toMinutes();

        long horasCobradas = Math.max(
                1,
                (long) Math.ceil(minutos / 60.0)
        );

        BigDecimal tarifa;

        if (tipoVeiculo == TipoVeiculo.CARRO) {
            tarifa = TARIFA_CARRO;
        } else if (tipoVeiculo == TipoVeiculo.MOTO) {
            tarifa = TARIFA_MOTO;
        } else {
            throw new IllegalArgumentException(
                    "Tipo de veículo inválido."
            );
        }

        return tarifa
                .multiply(BigDecimal.valueOf(horasCobradas))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Long gerarProximoId() {

        return permanenciaRepository.listarTodas()
                .stream()
                .map(Permanencia::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1L;
    }
}
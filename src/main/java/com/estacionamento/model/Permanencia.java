package com.estacionamento.model;

import java.time.LocalDateTime;

public class Permanencia {
    private Long id;
    private Vaga vaga;
    private Veiculo veiculo;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private Double valorPago;

    public Permanencia(Long id, Vaga vaga, Veiculo veiculo) {

        this.id = id;
        this.vaga = vaga;
        this.veiculo = veiculo;
        this.entrada = LocalDateTime.now();

    }

    public Long getId() {
        return id;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public Double getValorPago() {
        return valorPago;
    }
    public void registrarSaida(Double valorPago) {
        this.saida = LocalDateTime.now();
        this.valorPago = valorPago;
    }
}

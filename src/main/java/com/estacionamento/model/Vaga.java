package com.estacionamento.model;

public class Vaga {

    private Long id;
    private String numero;
    private  boolean disponivel;
    private TipoVeiculo tipoVeiculo;

    public Vaga(Long id, String numero, boolean disponivel, TipoVeiculo tipoVeiculo) {
        this.id = id;
        this.numero = numero;
        this.disponivel = true;
        this.tipoVeiculo = tipoVeiculo;
    }

    public long getId() {
        return id;
    }
    public String getNumero() {
        return numero;
    }
    public boolean isDisponivel() {
        return disponivel;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }
}

package com.estacionamento.model;

public class Vaga {

    private Long id;
    private String numero;
    private  boolean disponivel;

    public Vaga(Long id, String numero, boolean disponivel) {
        this.id = id;
        this.numero = numero;
        this.disponivel = true;
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
}

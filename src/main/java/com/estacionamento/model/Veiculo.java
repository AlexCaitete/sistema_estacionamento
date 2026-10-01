package com.estacionamento.model;

public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private String marca;

    public Veiculo( long id, String placa, String modelo, String marca) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }
}

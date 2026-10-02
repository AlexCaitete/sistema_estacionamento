package com.estacionamento.repository;

import com.estacionamento.model.Permanencia;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PermanenciaRepository {

    private final List<Permanencia> permanencias = new ArrayList<>();

    public void registrar(Permanencia permanencia) {
        permanencias.add(permanencia);
    }

    public List<Permanencia> listarTodas() {
        return permanencias;
    }

    public List<Permanencia> buscarAbertas() {
        return permanencias.stream()
                .filter(permanencia -> permanencia.getSaida() == null)
                .toList();
    }

    public List<Permanencia> buscarHistoricoPorPlaca(String placa) {
        return permanencias.stream()
                .filter(permanencia ->
                        permanencia.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .toList();
    }
}
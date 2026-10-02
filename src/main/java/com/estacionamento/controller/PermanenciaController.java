package com.estacionamento.controller;

import com.estacionamento.model.Permanencia;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/permanencias")
public class PermanenciaController {

    private final PermanenciaService service;

    public PermanenciaController(PermanenciaService service) {
        this.service = service;
    }

    @PostMapping("/entrada")
    public Permanencia registrarEntrada(@RequestBody Map<String, Long> dados) {
        return service.registrarEntrada(
                dados.get("idVeiculo"),
                dados.get("idVaga"));
    }

    @PostMapping("/{id}/saida")
    public Permanencia registrarSaida(@PathVariable Long id) {
        return service.registrarSaida(id);
    }

    @GetMapping("/{id}/valor")
    public BigDecimal consultarValor(@PathVariable Long id) {
        return service.consultarValor(id);
    }

    @GetMapping
    public List<Permanencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Permanencia buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

}//finalPermanenciaController

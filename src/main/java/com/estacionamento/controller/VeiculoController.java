package com.estacionamento.controller;

import com.estacionamento.model.Veiculo;
import com.estacionamento.service.VeiculoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public Veiculo cadastrar(@RequestBody Veiculo veiculo) {
        return service.cadastrar(veiculo);
    }

    @GetMapping
    public List<Veiculo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Veiculo buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}

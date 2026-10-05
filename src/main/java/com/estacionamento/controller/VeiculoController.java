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
    public void cadastrar(@RequestBody Veiculo veiculo) {
         service.cadastrar(veiculo);
    }

    @GetMapping
    public List<Veiculo> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Veiculo buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}

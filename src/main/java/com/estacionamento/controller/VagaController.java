package com.estacionamento.controller;
import com.estacionamento.model.TipoVeiculo;
import com.estacionamento.model.Vaga;
import com.estacionamento.service.VagaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/vagas")
public class VagaController {

     private final VagaService vagaService;


     public VagaController(VagaService vagaService) {
         this.vagaService = vagaService;
     }

     @PostMapping
     public void cadastrar(@RequestBody Vaga vaga){
            vagaService.cadastrar(vaga);
     }

    @GetMapping public List<Vaga> listarTodos() {
         return vagaService.listarTodos();
     }

     @GetMapping("/{id}")
     public Vaga buscarPorId(@PathVariable Long id) {
         return vagaService.buscarPorId(id);
     }
     @GetMapping("/lotacao/{tipoVeiculo}")
     public void verificarLotacao(@PathVariable TipoVeiculo tipoVeiculo) {
         vagaService.verificarLotacao(tipoVeiculo);
     }

}

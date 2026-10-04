package com.estacionamento.service;

import com.estacionamento.model.TipoVeiculo;
import com.estacionamento.model.Vaga;
import com.estacionamento.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    public void cadastrar(Vaga vaga) {
        vagaRepository.cadastrar(vaga);
    }

    public List<Vaga> listarTodos() {
        return vagaRepository.listarTodos();
    }

    public Vaga buscarPorId(Long id) {
        return vagaRepository.buscarPorId(id);
    }

    public void verificarLotacao(TipoVeiculo tipoVeiculo) {

        List<Vaga> vagas = vagaRepository.listarTodos();

        int totalVagas = 0;
        int vagasOcupadas = 0;

        for (Vaga vaga : vagas) {

            if (vaga.getTipoVeiculo() == tipoVeiculo) {

                totalVagas++;

                if (!vaga.isDisponivel()) {
                    vagasOcupadas++;
                }
            }
        }

        if (totalVagas == 0) {
            throw new IllegalStateException(
                    "Não existem vagas cadastradas para " + tipoVeiculo + "."
            );
        }

        if (vagasOcupadas == totalVagas) {
            throw new IllegalStateException(
                    "Estacionamento lotado para veículos do tipo "
                            + tipoVeiculo + "."
            );
        }
    }
}
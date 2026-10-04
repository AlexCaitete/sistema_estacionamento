package com.estacionamento.service;

import com.estacionamento.model.Veiculo;
import com.estacionamento.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public void cadastrar(Veiculo veiculo) {

        Veiculo veiculoExistente =
                veiculoRepository.buscarPorPlaca(veiculo.getPlaca());

        if (veiculoExistente != null) {
            throw new IllegalStateException(
                    "Já existe um veículo cadastrado com essa placa."
            );
        }

        veiculoRepository.cadastrar(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.listarTodos();
    }

    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.buscarPorId(id);
    }

    public Veiculo buscarPorPlaca(String placa) {
        return veiculoRepository.buscarPorPlaca(placa);
    }
}
package com.estacionamento.repository;

import com.estacionamento.model.Veiculo;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.ArrayList;

@Repository
public class VeiculoRepository {
    private List<Veiculo> veiculos = new ArrayList<>();

    public void cadastrar(Veiculo veiculo) {
         veiculos.add(veiculo);
   }
    public List<Veiculo> listarTodos() {
        return veiculos;
    }
    public Veiculo buscarPorId(Long id) {
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getId().equals(id)){
                return veiculo;
            }

        }
        return null;
    }
    public Veiculo buscarPorPlaca(String placa) {
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getPlaca().equals(placa)) {
                return veiculo;
            }

        }
        return null;
    }
}



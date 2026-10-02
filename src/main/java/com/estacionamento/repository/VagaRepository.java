package com.estacionamento.repository;
import com.estacionamento.model.Vaga;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class VagaRepository {
    private List<Vaga> vagas = new ArrayList<>();

    public void cadastrar(Vaga vaga) {
        vagas.add(vaga);
    }

    public List<Vaga>listarTodos() {
        return vagas;
    }
    public Vaga buscarPorId(Long id) {
        for (Vaga vaga : vagas) {
            if (vaga.getId() == id) {
                return vaga;
            }
        }
        return null;
    }
}

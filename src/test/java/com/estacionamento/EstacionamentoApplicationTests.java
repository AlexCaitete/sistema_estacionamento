package com.estacionamento;

import com.estacionamento.model.Permanencia;
import com.estacionamento.model.TipoVeiculo;
import com.estacionamento.model.Vaga;
import com.estacionamento.model.Veiculo;
import com.estacionamento.repository.PermanenciaRepository;
import com.estacionamento.repository.VagaRepository;
import com.estacionamento.repository.VeiculoRepository;
import com.estacionamento.service.PermanenciaService;
import com.estacionamento.service.VagaService;
import com.estacionamento.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EstacionamentoApplicationTests {

    private final VeiculoRepository veiculoRepository = new VeiculoRepository();
    private final VagaRepository vagaRepository = new VagaRepository();
    private final PermanenciaRepository permanenciaRepository = new PermanenciaRepository();

    private final VeiculoService veiculoService =
            new VeiculoService(veiculoRepository);
    private final VagaService vagaService =
            new VagaService(vagaRepository);
    private final PermanenciaService permanenciaService =
            new PermanenciaService(
                    permanenciaRepository,
                    vagaRepository,
                    veiculoRepository
            );

    @Test
    void contextLoads() {
    }

    @Test
    void deveCadastrarVeiculoComPlacaUnica() {
        Veiculo veiculo = new Veiculo(
                1L,
                "ABC1234",
                "Civic",
                "Honda",
                TipoVeiculo.CARRO
        );

        veiculoService.cadastrar(veiculo);

        assertEquals(veiculo, veiculoRepository.buscarPorPlaca("ABC1234"));
    }

    @Test
    void naoDeveCadastrarDoisVeiculosComAMesmaPlaca() {
        Veiculo veiculo = new Veiculo(
                2L,
                "XYZ9999",
                "Corolla",
                "Toyota",
                TipoVeiculo.CARRO
        );
        veiculoService.cadastrar(veiculo);

        Veiculo veiculoDuplicado = new Veiculo(
                3L,
                "xyz9999",
                "Civic",
                "Honda",
                TipoVeiculo.CARRO
        );

        assertThrows(
                IllegalStateException.class,
                () -> veiculoService.cadastrar(veiculoDuplicado)
        );
    }

    @Test
    void deveRegistrarEntradaQuandoVagaEstiverDisponivel() {
        Vaga vaga = new Vaga(10L, "A-01", true, TipoVeiculo.CARRO);
        Veiculo veiculo = new Veiculo(
                10L,
                "QWE4567",
                "Gol",
                "Volkswagen",
                TipoVeiculo.CARRO
        );
        vagaService.cadastrar(vaga);
        veiculoService.cadastrar(veiculo);

        Permanencia permanencia = permanenciaService.registrarEntrada(
                veiculo.getId(),
                vaga.getId()
        );

        assertNotNull(permanencia);
        assertEquals(vaga.getId(), permanencia.getVaga().getId());
        assertFalse(vaga.isDisponivel());
    }

    @Test
    void naoDevePermitirEntradaEmVagaOcupada() {
        Vaga vaga = new Vaga(11L, "B-02", false, TipoVeiculo.MOTO);
        Veiculo veiculo = new Veiculo(
                11L,
                "UIO7654",
                "Biz",
                "Honda",
                TipoVeiculo.MOTO
        );
        vagaService.cadastrar(vaga);
        veiculoService.cadastrar(veiculo);

        assertThrows(
                IllegalStateException.class,
                () -> permanenciaService.registrarEntrada(
                        veiculo.getId(),
                        vaga.getId()
                )
        );
    }

    @Test
    void naoDevePermitirVeiculoEmVagaDeTipoIncompativel() {
        Vaga vaga = new Vaga(12L, "C-03", true, TipoVeiculo.MOTO);
        Veiculo veiculo = new Veiculo(
                12L,
                "ABC4321",
                "Celta",
                "Chevrolet",
                TipoVeiculo.CARRO
        );
        vagaService.cadastrar(vaga);
        veiculoService.cadastrar(veiculo);

        assertThrows(
                IllegalStateException.class,
                () -> permanenciaService.registrarEntrada(
                        veiculo.getId(),
                        vaga.getId()
                )
        );
    }

    @Test
    void deveCalcularValorDaPermanenciaNaSaida() {
        Vaga vaga = new Vaga(13L, "D-04", true, TipoVeiculo.CARRO);
        Veiculo veiculo = new Veiculo(
                13L,
                "MNO2587",
                "Fiesta",
                "Ford",
                TipoVeiculo.CARRO
        );
        vagaService.cadastrar(vaga);
        veiculoService.cadastrar(veiculo);

        Permanencia permanencia = permanenciaService.registrarEntrada(
                veiculo.getId(),
                vaga.getId()
        );
        permanencia.getEntrada();
        permanencia.registrarSaida(30.00);

        assertEquals(30.00, permanencia.getValorPago());
        assertNotNull(permanencia.getSaida());
    }

    @Test
    void deveCalcularFaturamentoDoPeriodo() {
        Vaga vagaCarro = new Vaga(14L, "E-05", true, TipoVeiculo.CARRO);
        Veiculo veiculoCarro = new Veiculo(
                14L,
                "PQR1111",
                "Onix",
                "Chevrolet",
                TipoVeiculo.CARRO
        );
        Vaga vagaMoto = new Vaga(15L, "F-06", true, TipoVeiculo.MOTO);
        Veiculo veiculoMoto = new Veiculo(
                15L,
                "STU2222",
                "CG",
                "Honda",
                TipoVeiculo.MOTO
        );
        vagaService.cadastrar(vagaCarro);
        vagaService.cadastrar(vagaMoto);
        veiculoService.cadastrar(veiculoCarro);
        veiculoService.cadastrar(veiculoMoto);

        Permanencia permanenciaCarro = permanenciaService.registrarEntrada(
                veiculoCarro.getId(),
                vagaCarro.getId()
        );
        permanenciaCarro.registrarSaida(20.00);

        Permanencia permanenciaMoto = permanenciaService.registrarEntrada(
                veiculoMoto.getId(),
                vagaMoto.getId()
        );
        permanenciaMoto.registrarSaida(10.00);

        LocalDateTime inicio = LocalDateTime.now().minusMinutes(1);
        LocalDateTime fim = LocalDateTime.now().plusMinutes(1);

        assertEquals(
                new BigDecimal("30.00"),
                permanenciaService.calcularFaturamento(inicio, fim)
        );
    }
}

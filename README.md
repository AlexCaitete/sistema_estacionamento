# Sistema de Controle de Estacionamento 🚗🅿️

## 1. Informações da equipe

- **Repositório:** https://github.com/AlexCaitete/sistema_estacionamento
- **Disciplina:** Back-End
- **Unidade:** 1
- **Formato:** projeto em equipe com 7 participantes
- **Entrega:** repositório público no GitHub e apresentação curta

### Integrantes e funções

| Integrante                        | Função no projeto                                                                                 |
| --------------------------------- | ------------------------------------------------------------------------------------------------- |
| **Alex Alves Caitete**            | Coordenação geral, modelagem das entidades, tipos, documentação e ajustes finais do projeto.      |
| **João Vitor Moreira dos Santos** | Implementação do `VeiculoController`, responsável pelas rotas de cadastro e consulta de veículos. |
| **Mateus Mesquita**               | Implementação do `PermanenciaService`, incluindo cálculo de faturamento e regras da permanência.  |
| **Nildson Nascimento**            | Implementação dos controllers de vagas e permanências, além do fluxo de entrada e saída.          |
| **Arielle Domingos**              | Implementação dos serviços de vaga e veículo e da lógica das classes de negócio.                  |
| **Luiz Sérgio Ribeiro Pereira**   | Implementação dos repositories de vagas e veículos.                                               |
| **Jonatas Lima**                  | Implementação da `PermanenciaRepository` e armazenamento das permanências.                        |


## 2. Descrição do projeto

Este projeto implementa um sistema de controle de estacionamento usando Java 21, Spring Boot, Maven e testes automatizados com JUnit. A aplicação controla veículos, vagas, entradas e saídas, calcula o valor da permanência e gera o faturamento de um período.

A persistência é feita em memória por meio de `ArrayList`, conforme exigido pela avaliação. Não são utilizadas banco de dados, JPA, Hibernate, JDBC, Docker, autenticação ou frameworks adicionais.

## 3. Funcionalidades implementadas

- Cadastrar veículo;
- Listar veículos e consultar por ID;
- Cadastrar vaga;
- Listar vagas e consultar vagas por ID;
- Verificar lotação por tipo de veículo;
- Registrar entrada com validações;
- Registrar saída e liberar a vaga;
- Consultar valor da permanência;
- Listar permanências;
- Calcular faturamento de um intervalo de datas;
- Calcular faturamento diário;
- Consultar histórico de permanências por placa.

## 4. Regras de negócio

- Uma placa não pode ser cadastrada mais de uma vez;
- Um veículo deve existir antes de registrar sua entrada;
- Uma vaga deve existir antes de registrar uma entrada;
- Uma vaga ocupada não pode receber outro veículo;
- Um veículo não pode ter mais de uma permanência aberta;
- A vaga deve aceitar o tipo de veículo informado;
- O fechamento da permanência calcula o valor de acordo com a tarifa por hora;
- O faturamento não permite data final anterior à data inicial;
- O valor da permanência não pode ser consultado antes da saída;
- A lotação é verificada separadamente para carro e moto;
- Quando não houver vagas de um tipo, a aplicação retorna uma exceção informando a lotação.

## 5. Arquitetura

A aplicação segue a separação de responsabilidades exigida pela avaliação:

```text
src/main/java/com/estacionamento
├── controller/   HTTP e entrada/saída
├── model/        Entidades e enumerações
├── repository/   Armazenamento em memória
└── service/      Regras de negócio e validações
```

### Model

- `Veiculo`
- `Vaga`
- `Permanencia`
- `TipoVeiculo`

### Repository

Os repositories usam `ArrayList` para armazenar e recuperar dados temporários.

### Service

Os serviços contêm as validações, cálculos e regras de negócio.

### Controller

Os controllers expõem operações REST para interação com a aplicação.

## 6. Endpoints da API

### Veículos

| Método | Rota             | Função                  |
| ------ | ---------------- | ----------------------- |
| `POST` | `/veiculos`      | Cadastra um veículo     |
| `GET`  | `/veiculos`      | Lista veículos          |
| `GET`  | `/veiculos/{id}` | Consulta veículo por ID |

### Vagas

| Método | Rota                           | Função                             |
| ------ | ------------------------------ | ---------------------------------- |
| `POST` | `/vagas`                       | Cadastra uma vaga                  |
| `GET`  | `/vagas`                       | Lista vagas                        |
| `GET`  | `/vagas/{id}`                  | Consulta vaga por ID               |
| `GET`  | `/vagas/lotacao/{tipoVeiculo}` | Verifica lotação do tipo informado |

### Permanências

| Método | Rota                                           | Função                                      |
| ------ | ---------------------------------------------- | ------------------------------------------- |
| `POST` | `/permanencias/entrada`                        | Registra entrada com `idVeiculo` e `idVaga` |
| `PUT`  | `/permanencias/{id}/saida`                     | Registra saída                              |
| `GET`  | `/permanencias/{id}/valor`                     | Consulta valor pago                         |
| `GET`  | `/permanencias`                                | Lista permanências                          |
| `GET`  | `/permanencias/faturamento?inicio=...&fim=...` | Calcula faturamento de um período           |
| `GET`  | `/permanencias/{id}`                           | Consulta permanência por ID                 |

Exemplo de corpo para entrada:

```json
{
  "idVeiculo": 1,
  "idVaga": 1
}
```

## 7. Tecnologias

- Java 21
- Spring Boot 4.1.1
- Maven
- JUnit 5
- Git e GitHub

## 8. Como executar

### Requisitos

- Java 21
- Maven 3.9 ou superior
- Git

### Executar pelo Maven Wrapper no Windows

```powershell
./mvnw.cmd spring-boot:run
```

### Executar testes

```powershell
./mvnw.cmd test
```

### Executar testes com limpeza

```powershell
./mvnw.cmd clean test
```

A aplicação ficará disponível em `http://localhost:8080`.

## 9. Testes automatizados

A suíte contém testes para:

- Carregamento do contexto do Spring;
- Cadastro de veículos com placa única;
- Impedimento de cadastro duplicado;
- Entrada em vaga disponível;
- Entrada em vaga ocupada;
- Compatibilidade entre tipo de vaga e veículo;
- Cálculo do valor da permanência;
- Cálculo do faturamento do período.

A execução atual da suíte foi validada com **8 testes executados, 0 falhas, 0 erros e 0 testes ignorados**.

## 10. Histórico de desenvolvimento

O projeto mantém o histórico no Git e possui commits na branch `main`. O repositório também contém o histórico de merge e de implementação das camadas.

Para consultar o histórico local:

```powershell
git log --oneline --decorate --graph -10
```

## 11. Uso de inteligência artificial

A implementação foi desenvolvida e revisada com apoio de ferramentas de inteligência artificial para análise de código, identificação de erros e melhoria da organização do projeto. A IA foi utilizada somente como auxiliar de desenvolvimento e revisão; todos os requisitos e regras de negócio foram avaliados antes da alteração.

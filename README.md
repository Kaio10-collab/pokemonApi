# ⚡ Pokémon API Consumer (Java 21 & Spring Boot) ⚡

Este projeto é uma API RESTful desenvolvida em **Java 21** com **Spring Boot** que consome a API pública da Pokémon (PokeAPI) e expõe um endpoint simples para buscar informações de Pokémon pelo nome. Ele foi construído seguindo os princípios da **Clean Architecture** para garantir modularidade, testabilidade e fácil manutenção.

---

## 🚀 Tecnologias Utilizadas

* **Java 21**: A versão mais recente da linguagem, com recursos modernos.
* **Spring Boot 3.3.1**: Framework para desenvolvimento rápido de aplicações Java.
* **Maven**: Ferramenta de automação de construção e gerenciamento de dependências.
* **WebClient (Spring WebFlux)**: Cliente HTTP reativo para consumo assíncrono da PokeAPI.
* **Lombok**: Biblioteca para reduzir código boilerplate (getters, setters, construtores).
* **JUnit 5 & Mockito**: Ferramentas para testes unitários e de integração.
* **Clean Architecture**: Padrão arquitetural para um design de software robusto e desacoplado.

---

## 🎯 Arquitetura do Projeto (Clean Architecture)

O projeto está organizado em camadas concêntricas, onde as dependências fluem das camadas externas (detalhes de implementação) para as camadas internas (regras de negócio).

🎯 Arquitetura do Projeto (Clean Architecture adaptada)
O projeto pokemon-api segue os princípios da Clean Architecture, organizando o código em camadas que promovem o desacoplamento e a testabilidade. Eu adaptei a arquitetura de acordo com cada camada e associei com as camadas definida por Uncle Bob.  A estrutura de pacotes reflete essas camadas e suas responsabilidades:

pokemon-api
├── src
│   └── main
│       └── java
│           └── estudo  // Pacote raiz da sua aplicação
│               └── pokemon
│                   ├── adapter          (Implementações de adaptadores externos, mapeamento)
│                   │   └── config       (WebClientConfig.java)
│                   │   └── dataprovider (PokeApiDataProvider.java)  <- Implementa o gateway
│                   ├── entrypoint       (Camada de interface/entrada)
│                   │   ├── controller   (PokemonController.java)
│                   │   └── response     (PokemonResponse.java)     <- DTO de saída da API
│                   ├── response         (DTOs de resposta de serviços externos)
│                   │   └── PokeApiPokemonResponse.java             <- DTO da PokeAPI
│                   ├── entity           (Entidades de Domínio)
│                   │   └── Pokemon.java
│                   ├── usecase          (Casos de Uso)
│                   │   └── GetPokemonUseCase.java
│                   ├── gateway          (Portas/Interfaces do Domínio)
│                   │   └── PokemonRepositoryPort.java
│                   └── ApiApplication.java (Classe principal do Spring Boot)
└── pom.xml

🎯 Explicação das Camadas e Pacotes:
entity (Domain - Entidades):

Pokemon.java: Representa a entidade central de domínio, pura e sem dependências de frameworks ou detalhes de infraestrutura. Ela define o que um Pokémon é para a nossa aplicação.

gateway (Domain - Portas):

PokemonRepositoryPort.java: Define a interface (ou "porta") para a obtenção de dados de Pokémon. A camada de caso de uso (Application) interage com essa interface, sem se importar com a implementação concreta (que estará no adapter/dataprovider).

usecase (Application - Casos de Uso):

GetPokemonUseCase.java: Contém a lógica de negócio específica para "obter um Pokémon". Ele orquestra as operações, utilizando a porta PokemonRepositoryPort para acessar os dados.

adapter (Infrastructure - Adaptadores):

Este pacote agrupa as implementações concretas que "adaptam" as interfaces do domínio para tecnologias ou serviços externos.

config:

WebClientConfig.java: Configura o cliente HTTP (WebClient) para interagir com a PokeAPI.

dataprovider:

PokeApiDataProvider.java: Esta classe é a implementação concreta de PokemonRepositoryPort. Ela é responsável por fazer a chamada real à PokeAPI, receber a resposta e mapeá-la para a entidade de domínio Pokemon. É o "adaptador" que preenche a porta PokemonRepositoryPort.

response (DTOs Externos):

PokeApiPokemonResponse.java: Um DTO (Data Transfer Object) específico para mapear a estrutura da resposta JSON da PokeAPI. Este DTO é um detalhe de infraestrutura, utilizado apenas pelo PokeApiDataProvider.

entrypoint (Presentation - Pontos de Entrada):

Este pacote representa a camada de apresentação/interface da sua API.

controller:

PokemonController.java: É o ponto de entrada da sua API REST. Ele recebe as requisições HTTP, valida entradas, chama o GetPokemonUseCase e formata a resposta para o cliente.

response:

PokemonResponse.java: Este DTO é usado para definir a estrutura da resposta JSON que a sua API enviará para o cliente. É uma representação pública dos dados do Pokémon.

ApiApplication.java:

A classe principal do Spring Boot que inicia a aplicação.

## 🎯Fluxo de Funcionamento (Adaptado à sua Estrutura):
Uma requisição HTTP GET /pokemon/{name} chega ao PokemonController (dentro de entrypoint/controller).

O PokemonController chama o GetPokemonUseCase (dentro de usecase).

O GetPokemonUseCase invoca o método getPokemonByName da interface PokemonRepositoryPort (dentro de gateway).

A implementação concreta de PokemonRepositoryPort, que é o PokeApiDataProvider (dentro de adapter/dataprovider), é utilizada.

O PokeApiDataProvider faz a requisição HTTP para a PokeAPI, utilizando o WebClient (configurado em adapter/config/WebClientConfig).

A resposta da PokeAPI é desserializada para o DTO PokeApiPokemonResponse (dentro de response).

O PokeApiDataProvider mapeia o PokeApiPokemonResponse para a entidade de domínio Pokemon (dentro de entity) e a retorna.

O GetPokemonUseCase recebe a entidade Pokemon e a retorna para o PokemonController.

O PokemonController mapeia a entidade Pokemon para o DTO de resposta da sua API, PokemonResponse (dentro de entrypoint/response), e envia a resposta HTTP ao cliente.

---

## 🧪 Testes

O projeto inclui testes unitários para as camadas `usecase` (Casos de Uso) e `adapter` (Adaptadores), além de testes de integração leves para a camada `controller` (Controladores) utilizando `MockMvc`.

---

## 🗺️ Endpoints da API:
A API expõe o seguinte endpoint para buscar informações de Pokémon:

GET /pokemon/{name}
Descrição: Busca os detalhes de um Pokémon específico pelo seu nome.

URL: http://localhost:8080/pokemon/{name}

Método: GET

Parâmetros de Caminho:
name (string, obrigatório): O nome do Pokémon (ex: pikachu, charmander). O nome será automaticamente convertido para minúsculas antes de consultar a PokeAPI.

Exemplo de Requisição:

GET http://localhost:8080/pokemon/pikachu
Exemplo de Resposta (Status 200 OK):

JSON

{
    "name": "pikachu",
    "height": 4,
    "weight": 60,
    "abilities": [
        "static",
        "lightning-rod"
    ],
    "imageUrl": "[https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png](https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png)"
}

Exemplo de Resposta (Status 404 Not Found):
Quando o Pokémon não for encontrado na PokeAPI.
(Corpo vazio)

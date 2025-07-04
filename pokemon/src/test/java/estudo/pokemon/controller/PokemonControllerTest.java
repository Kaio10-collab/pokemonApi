package estudo.pokemon.controller;

import estudo.pokemon.adapter.entrypoint.controller.PokemonController;
import estudo.pokemon.entity.Pokemon;
import estudo.pokemon.usecase.GetPokemonUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokemonController.class) // anotação para testar apenas a camada web(controller no caso)
public class PokemonControllerTest {

    /*
    Classe de teste de integração.
    */
    @Autowired // o spring injeta uma instância de MockMvc, que vamos simular requisições HTTP
    private MockMvc mockMvc; // Isso é um objeto para simulamos requisições HTTP

    @MockitoBean // Cria um mock e o injeta no contexto do Spring Boot para esse teste
    private GetPokemonUseCase useCase;

    @Test
    @DisplayName("Should return ok and pokemon data when found")
        // Deve retornar 200 ok e os dados do pokémon quando encontrado
    void shouldReturnOkAndPokemonDataWhenFound() throws Exception {
        // Cenario (Given)
        var pokemonName = "charmander";
        Pokemon charmander = Pokemon.builder()
                .name("charmander")
                .height(6)
                .weight(85)
                .abilities(Collections.singletonList("blaze"))
                .imageUrl("http://pokeapi.co/sprites/charmander.png")
                .build();

        // Vamos configurar o comportamento do mock do useCase, para quando o método execute for chamado, retornar um Optional contendo um pokémon
        when(useCase.execute(pokemonName)).thenReturn(Optional.of(charmander));

        // Ação (When) e também Verificaçào(Then)
        mockMvc.perform(get("/pokemon/{name}", pokemonName) // iremos simular uma requisição GET para o endpoint
                        .content(String.valueOf(MediaType.APPLICATION_JSON))) // Define o tipo de conteúdo da requisição
                .andExpect(status().isOk()) // Espera um status HTTP 200 OK
                .andExpect(jsonPath("$.name").value("charmander")) // Verifica o valor do campo "name" no JSON
                .andExpect(jsonPath("$.height").value(6)) // Verifica o valor do campo "heigt"
                .andExpect(jsonPath("$.weight").value(85)) // Verifica o valor do campo "weight"
                .andExpect(jsonPath("$.abilities[0]").value("blaze")) // Verifica o primeiro elemento da lista "abilities"
                .andExpect(jsonPath("$.imageUrl").value("http://pokeapi.co/sprites/charmander.png")); // Verifica o campo "imageUrl"
    }

    @Test
    @DisplayName("shouldReturnNotFoundWhenPokemonNotfound")
        // Deve retornar 404 not found quando o pokémon não for encontrado
    void shouldReturnNotFoundWhenPokemonNotfound() throws Exception {
        // Cenário (Given)
        var pokemonName = "nonexistent";

        // Vamos configurar o comportamento do mock do useCase, para quando o método execute for chamado, retornar um Optional vazio
        when(useCase.execute(pokemonName)).thenReturn(Optional.empty());

        // Ação (When) e Verificação (Then)
        mockMvc.perform(get("/pokemon/{name}", pokemonName)
                        .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound());
    }
}

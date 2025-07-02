package estudo.pokemon.usecase;

import estudo.pokemon.entity.Pokemon;
import estudo.pokemon.usecase.gateway.PokemonRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Vamos habilitar a extensão Mockito para JUnit 5, ela permite o uso de anotações como o Mock e injectMocks
class GetPokemonUseCaseTest {

    @Mock // Criaremos um mock da interface, um mock é um objeto simulado que podemos controlar para testar como o nosso código interage com suas dependências
    private PokemonRepositoryPort repository;

    @InjectMocks // vamos injectar os mocks, no caso a classe da interface, na instância de GetPokemonUseCase
    private GetPokemonUseCase useCase;

    @Test // ele marca o método como um m[etodo de teste JUnit
    @DisplayName("Should return pokemon when found by name") // Permite dar nomes mais legíveis aos testes, sendo útil para relatórios e entendimento.
    void shouldReturnPokemonWhenFoundByName(){ // Deve retornar um pokémon quando encontrado pelo nome.
        // Cenário(Given)
        var pokemonName = "Pikachu";
        Pokemon expectedPokemon = Pokemon.builder()
                .name("Pikahu")
                .height(4)
                .weight(60)
                .abilities(Collections.singletonList("static"))
                .imageUrl("some-url")
                .build();

        // Vamos configurar o comportamento do mock: Quando o useCase for chamado com "Pikachu"
        // vai retornar um Optional contendo o expectedPokemon
        // Basicamente estamos dizendo para se comportar assim "Quando o método getPokemonByName for chamado no repository com o podemonName especifico, retorne esse Optional."
        when(repository.getPokemonByName(pokemonName)).thenReturn(Optional.of(expectedPokemon));

        // Ação(When)
        Optional<Pokemon> result = useCase.execute(pokemonName);

        // Verificação (Then)
        assertTrue(result.isPresent()); // Vamos verificar se o resultado não esta vazio
        assertEquals(expectedPokemon, result.get()); // Verificamos se o pok[emon retornando é o esperado
        verify(repository).getPokemonByName(pokemonName); // Vamos verificar se o método getPokemonByName foi chamado exatamente uma vez com o nome correto
    }

    @Test
    @DisplayName("Should return pokemon when found by name") // Deve retornar Optional vazio quando o pokémon não for encontrado
    void shouldReturnEmptyOptionalWhenPokemonNotFound(){
        // Cenário(Given)
        var pokemonName = "nonexistent";

        // Vamos configurar o comportamento do mock: Quando o useCase for chamado com "nonexistent"
        // vai retornar um Optional vazio
        when(repository.getPokemonByName(pokemonName)).thenReturn(Optional.empty());

        // Ação(When)
        Optional<Pokemon> result = useCase.execute(pokemonName);

        // Verificação (Then)
        assertTrue(result.isEmpty()); // Vamos verificar se o resultado esta vazio
        verify(repository).getPokemonByName(pokemonName); // Vamos verificar se o método getPokemonByName foi chamado exatamente uma vez com o nome correto
    }

}

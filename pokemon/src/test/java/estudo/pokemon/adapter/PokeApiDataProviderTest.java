package estudo.pokemon.adapter;

import estudo.pokemon.adapter.dataprovider.PokeApiDataProvider;
import estudo.pokemon.adapter.response.PokeApiPokemonResponse;
import estudo.pokemon.entity.Pokemon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokeApiDataProviderTest {

    @Mock
    private WebClient webClient; // mock do WebClient principal

    // Mocks para as chamadas encadadas do WebClient
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PokeApiDataProvider provider;

    /*
    Como interagimos com um cliente HTTP, precisaremos testar se ele faz a requisição corretamente, lida com respostas de sucesso e com erros.
    Mockar o WebClient requer mais detalhes, pois precisaremso simular as chamadas encadeadas que coloquei acima.
    */

    @Test
    @DisplayName("Should return mapped pokemon on successul response")
    void shouldReturnMappedPokemonOnSuccessulResponse() { // Deve retornar um Pokémon mapeado corretamente de uma resposta bem-sucedida da PokeApi
        // Cenário (Given)
        var pokemonName = "charmander";
        var lowerCaseName = pokemonName.toLowerCase();

        // Vamos criar uma DTO simulado da PokeApi
        PokeApiPokemonResponse mockApiResponse = new PokeApiPokemonResponse();
        mockApiResponse.setName("charmander");
        mockApiResponse.setHeight(6);
        mockApiResponse.setWeight(85);
        mockApiResponse.setAbilities(Arrays.asList(
                createAbility("blaze"),
                createAbility("solar-power")
        ));
        mockApiResponse.setSprites(createSprites("http://pokeapi.co/sprites/charmander.png"));

        // Vamos configurar o mock do WebClient para simular o encadeamento de chamadas
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("pokemon/{name}", lowerCaseName)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        // Quando o bodyToMono for chamado, deve retornarn um Mono com o nosso DTO simulado
        when(responseSpec.bodyToMono(PokeApiPokemonResponse.class)).thenReturn(Mono.just(mockApiResponse));

        // Ação (When)
        Optional<Pokemon> result = provider.getPokemonByName(pokemonName);

        // Verificação (Then)
        assertTrue(result.isPresent());
        Pokemon pokemon = result.get();
        assertEquals("charmander", pokemon.getName());
        assertEquals(6, pokemon.getHeight());
        assertEquals(85, pokemon.getWeight());
        assertEquals(Arrays.asList("blaze", "solar-power"), pokemon.getAbilities());
        assertEquals("http://pokeapi.co/sprites/charmander.png", pokemon.getImageUrl());

        // Iremos verificar se o WebClient foi chamado corretamente
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("pokemon/{name}", lowerCaseName);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(PokeApiPokemonResponse.class);
    }

    // métodos auxiliares para crianos objetos de testes de forma mais limpa(respeitnado o principio solid)
    private PokeApiPokemonResponse.PokeApiAbility createAbility(String name) {
        PokeApiPokemonResponse.PokeApiAbility ability = new PokeApiPokemonResponse.PokeApiAbility();
        PokeApiPokemonResponse.PokeApiAbility.PokeApiAbilityDetail detail = new PokeApiPokemonResponse.PokeApiAbility.PokeApiAbilityDetail();

        detail.setName(name);
        ability.setAbility(detail);
        return ability;
    }

    private  PokeApiPokemonResponse.PokeApiSprites createSprites(String imageUrl) {
        PokeApiPokemonResponse.PokeApiSprites sprites = new PokeApiPokemonResponse.PokeApiSprites();
        sprites.setFrontDefault(imageUrl);
        return sprites;
    }
}

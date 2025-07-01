package estudo.pokemon.adapter.dataprovider;

import estudo.pokemon.adapter.response.PokeApiPokemonResponse;
import estudo.pokemon.entity.Pokemon;
import estudo.pokemon.usecase.gateway.PokemonRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component // Vamos marcar essa classe para ser um componente do Spring
public class PokeApiDataProvider implements PokemonRepositoryPort {

    /*
    Aqui vamos implementar a lógica do método da interface
    */
    private final WebClient webClient;

    // injeção de dependência do WebClient configurado, permitindo que o adaptador o utilize para fazer as requisições
    public PokeApiDataProvider(WebClient pokeApiWebClient) {
        this.webClient = pokeApiWebClient;
    }

    @Override
    public Optional<Pokemon> getPokemonByName(String name) {

        // Converter o nome para minusculo, pois a pokeapi utiliza os nomes em minusculos.
        var lowerCaseName = name.toLowerCase();

        // Iremos fazer a requisição HTTP para a PokeApi
        Mono<PokeApiPokemonResponse> responseMono = webClient.get() // usamos webclient para construir a requisição Get para a URL pokemon/{name}
                .uri("pokemon/{name}", lowerCaseName)
                .retrieve()
                .bodyToMono(PokeApiPokemonResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty()); // Se vier 404, retorna vazio

        // Bloqueia para obtermos o resultado
        return responseMono
                .map(this::mapToDomain) // Mapeia a resposta da API para a entidade de domínio
                .blockOptional(); // Bloqueia e retorna um Optional
    }

    // Método privado para mapear o DTO da PokeApi para a entidade de domínio
    private Pokemon mapToDomain(PokeApiPokemonResponse response) {

        List<String> abilities = response.getAbilities().stream()
                .map(a -> a.getAbility().getName())
                .toList();

        String imageUrl = null;

        if (response.getSprites() != null) {
            imageUrl = response.getSprites().getFrontDefault();
        }
        return Pokemon.builder()
                .name(response.getName())
                .height(response.getHeight())
                .weight(response.getWeight())
                .abilities(abilities)
                .imageUrl(imageUrl)
                .build();
    }
}
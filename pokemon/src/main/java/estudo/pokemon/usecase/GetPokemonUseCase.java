package estudo.pokemon.usecase;

import estudo.pokemon.entity.Pokemon;
import estudo.pokemon.usecase.gateway.PokemonRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Estamos dizendo ao Spring, "essa é uma classe de serviço e você deve gereciar a sua instância."
@AllArgsConstructor // É um injeção de dependência da classe PokemonRepositoryPort ou qualquer classe.
public class GetPokemonUseCase {

    /*
    Essa classe vai encapsular a lógica de obter um pokémon
    */

    private final PokemonRepositoryPort repositoryPort;

    public Optional<Pokemon> execute(String pokemonName) {
        //apenas chamaremos o repository, pois não precisamos nós preocupar com os detalhes de como os dados serão obtidos, apenas o contrato.
        return repositoryPort.getPokemonByName(pokemonName);
    }
}

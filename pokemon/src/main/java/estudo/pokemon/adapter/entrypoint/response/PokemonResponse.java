package estudo.pokemon.adapter.entrypoint.response;

import estudo.pokemon.entity.Pokemon;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PokemonResponse {

    /*
    Vamos criar uma DTO para a resposta da nossa propria API, que é diferente da entidade de domínio ou da DTO da PokeApi.
    Essa DTO define a nossa estrutura de resposta JSON que a nossa API enviará para o cliente.
    */

    private String name;
    private String imageUrl;
    private int height;
    private int weight;
    private List<String> abilities;

    // método estático para convertemos a entidade de domínio para a DTO de resposta
    public static PokemonResponse fromDomain(Pokemon pokemon) {
        return PokemonResponse.builder()
                .name(pokemon.getName())
                .height(pokemon.getHeight())
                .weight(pokemon.getWeight())
                .abilities(pokemon.getAbilities())
                .imageUrl(pokemon.getImageUrl())
                .build();
    }
}

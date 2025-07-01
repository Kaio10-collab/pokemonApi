package estudo.pokemon.usecase.gateway;

import estudo.pokemon.entity.Pokemon;

import java.util.Optional;

// É uma classe de interface para definimos um contrato para qualquer serviço qu queriamos fornecer dados do pokémon.
public interface PokemonRepositoryPort {

    Optional<Pokemon> getPokemonByName(String Name);
    // Optei por colocar um Optional para lidar com a possibilidade de que o pok[emon não ser encontrado
}

package estudo.pokemon.adapter.entrypoint.controller;

import estudo.pokemon.usecase.GetPokemonUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Aqui vamos indicar que essa classe aqui é um controlador REST
@RequestMapping("/pokemon") // Iremos definir o caminho base para todos os endpoints nesta classe
public class PokemonController {

    private final GetPokemonUseCase useCase;

    // Criaremos uma injeção de dependência do caso de uso
    public PokemonController(GetPokemonUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{name}") // vamos mapear as requisições GET para /pokemon/{name}
    public ResponseEntity<PokemonResponse> getPokemonByName(@PathVariable String name) { // Vamos extrair o valor da url e o injeta no parâmetro name no método
        return useCase.execute(name)
                .map(PokemonResponse::fromDomain) // Se encontramos, mapeia para a DTO da resposta
                .map(ResponseEntity::ok) // Retonra 200 OK com o corpo do pokemon
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Se não encontrou, retorna 404 Not found.
    }
}
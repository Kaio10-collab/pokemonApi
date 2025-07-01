package estudo.pokemon.adapter.entrypoint.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data // Gera getter, setter, toString, equals e hashCode
@JsonIgnoreProperties(ignoreUnknown = true) // Queremos ignorar campos JSON que não mapeamos, para evitar erros de desserialização
public class PokeApiPokemonResponse {

    /*
    Como a API retorna um JSON gigantesco com informações, será necessário criamos uma DTO para mapear apenas os campos que nós interessam.
    */

    private String name;
    private int height;
    private int weight;
    private List<PokeApiAbility> abilities;
    private PokeApiSprites sprites;

    // Aqui vou criar as classes aninhadas para extrair o JSON das habilidades dos pokémons e a URL da imagem deles.

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokeApiAbility {
        private PokeApiAbilityDetail ability;
        private boolean isHidden;
        private int slot;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PokeApiAbilityDetail {
            private String name;
            private String url;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokeApiSprites {
        private String frontDefault;
    }
}
package estudo.pokemon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Gera getter, setter, toString, equals e hashCode
@Builder // Gerar um constructor de builder para fácil criação de objetos
@NoArgsConstructor // Gerar um constructor sem argumentos
@AllArgsConstructor // Gerar um constructor com argumentos
public class Pokemon {

    /*
    Essa vai ser a classe que representa a entidade geral do nosso dominio.
    Onde iremos encapsular as informações de um Pokémon que nos interessam, como nome, altura, peso, habilidades e url da imagem.
    */

    private String name;
    private String imageUrl;
    private int height;
    private int weight;
    private List<String> abilities;
}

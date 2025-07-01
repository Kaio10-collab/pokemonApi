package estudo.pokemon.dataprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // Vamos dizer ao Spring, "Esta é uma classe de configuração."
public class WebClientConfig {

    /*
    Aqui vamos configurar a classe para fazer requisições para a api do pokémon, onde iremos consumir os dados dos pokémons.
    */

    private static final String pokeApiBaseUrl = "https://pokeapi.co/api/v2/";

    @Bean //Indica que o m[etodo retorna um bean que o Spring irá gerenciar
    public WebClient pokeApiWebClient() {
        return WebClient.builder()
                .baseUrl(pokeApiBaseUrl)
                .build();
    }
}

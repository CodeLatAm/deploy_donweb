package com.abel.sistema_gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig{
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    /*
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://apis.datos.gob.ar/georef/api")  // URL base de la API
                .build();
    }

     */
}

package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.Location.LocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LocationServiceImp {
    private final WebClient webClient;

    public LocationServiceImp(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apis.datos.gob.ar/georef/api").build();
    }

    public LocationResponse getLocationsByName(String nombre) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/localidades")
                        .queryParam("nombre", nombre)
                        .build())
                .retrieve()
                .bodyToMono(LocationResponse.class)
                .block();  // Puedes usar block() o regresar el Mono para un manejo más reactivo
    }
}

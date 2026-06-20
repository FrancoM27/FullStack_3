package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PerfilClient {

    private final WebClient webClient;

    public PerfilClient(@Qualifier("perfilWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Map<String, Object>> obtenerPerfil(Long usuarioId) {
        return webClient.get()
                .uri("/usuario/{usuarioId}", usuarioId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> crearPerfil(Map<String, Object> perfil) {
        return webClient.post()
                .uri("")
                .bodyValue(perfil)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> actualizarPerfil(Long usuarioId, Map<String, Object> perfil) {
        return webClient.put()
                .uri("/usuario/{usuarioId}", usuarioId)
                .bodyValue(perfil)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}

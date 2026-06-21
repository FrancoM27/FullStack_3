package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ResenaClient {

    private final WebClient webClient;

    public ResenaClient(@Qualifier("resenasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Map<String, Object>> obtenerResenasProducto(Long productoId) {
        return webClient.get()
                .uri("/producto/{productoId}", productoId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Flux<Map<String, Object>> obtenerResenasCliente() {
        return webClient.get()
                .uri("/cliente")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Flux<Map<String, Object>> obtenerResenasVendedor(Long vendedorId) {
        return webClient.get()
                .uri("/vendedor/{vendedorId}", vendedorId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> crearResena(Map<String, Object> resena) {
        return webClient.post()
                .uri("")
                .bodyValue(resena)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> responderResena(Long resenaId, String respuesta) {
        return webClient.put()
                .uri("/{resenaId}/responder", resenaId)
                .bodyValue(respuesta)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}

package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PagoClient {

    private final WebClient webClient;

    public PagoClient(@Qualifier("pagosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Map<String, Object>> obtenerCarrito(Long clienteId) {
        return webClient.get()
                .uri("/carrito/{clienteId}", clienteId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> agregarAlCarrito(Map<String, Object> item) {
        return webClient.post()
                .uri("/carrito/agregar")
                .bodyValue(item)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> iniciarPago(Map<String, Object> solicitud) {
        return webClient.post()
                .uri("/iniciar")
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> iniciarPagoDesdeCarrito(Long clienteId) {
        return webClient.post()
                .uri("/iniciar-desde-carrito/{clienteId}", clienteId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> confirmarPago(Long idPago) {
        return webClient.post()
                .uri("/confirmar/{idPago}", idPago)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}

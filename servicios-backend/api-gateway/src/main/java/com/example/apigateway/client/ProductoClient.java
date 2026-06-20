package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Qualifier("productosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Map<String, Object>> obtenerProducto(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Flux<Map<String, Object>> obtenerProductos() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Flux<Map<String, Object>> obtenerProductosPorVendedor(Long vendedorId) {
        return webClient.get()
                .uri("/vendedor/{vendedorId}", vendedorId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Void> restarStock(Long id, Integer cantidad) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/restar-stock")
                        .queryParam("cantidad", cantidad)
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class);
    }
}

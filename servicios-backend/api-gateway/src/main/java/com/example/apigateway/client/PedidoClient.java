package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PedidoClient {

    private final WebClient webClient;

    public PedidoClient(@Qualifier("pedidosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Map<String, Object>> obtenerMisPedidos() {
        return webClient.get()
                .uri("/mis-pedidos")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Flux<Map<String, Object>> obtenerPedidosVendedor(Long vendedorId) {
        return webClient.get()
                .uri("/vendedor/{vendedorId}", vendedorId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> validarCompra(Long clienteId, Long productoId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/estado")
                        .queryParam("productoId", productoId)
                        .build(clienteId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Void> cambiarEstado(Long id, String nuevoEstado) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/estado")
                        .queryParam("nuevoEstado", nuevoEstado)
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class);
    }
}

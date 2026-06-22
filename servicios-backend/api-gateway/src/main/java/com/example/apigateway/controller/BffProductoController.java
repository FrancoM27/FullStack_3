package com.example.apigateway.controller;

import com.example.apigateway.client.PedidoClient;
import com.example.apigateway.client.ProductoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bff/productos")
@CrossOrigin(origins = {"http://localhost:5173", "http://18.211.231.0", "http://18.211.231.0:5173"}, allowCredentials = "true")
public class BffProductoController {

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private PedidoClient pedidoClient;

    @GetMapping("/{id}/detalle-completo")
    public Mono<Map<String, Object>> obtenerProductoDetalleCompleto(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        return productoClient.obtenerProducto(id)
                .flatMap(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("producto", producto);

                    if (userId != null && authHeader != null) {
                        return pedidoClient.obtenerMisPedidos(userId)
                                .collectList()
                                .map(pedidos -> {
                                    boolean haComprado = false;
                                    boolean entregado = false;

                                    for (Map<String, Object> pedido : pedidos) {
                                        Number pedProdId = (Number) pedido.get("productoId");

                                        if (pedProdId != null &&
                                                pedProdId.longValue() == id.longValue() &&
                                                "ENTREGADO".equals(pedido.get("estado"))) {

                                            haComprado = true;
                                            entregado = true;
                                            break;
                                        }
                                    }

                                    response.put("haComprado", haComprado);
                                    response.put("entregado", entregado);
                                    return response;
                                })
                                .onErrorResume(e -> {
                                    response.put("haComprado", false);
                                    response.put("entregado", false);
                                    return Mono.just(response);
                                });
                    } else {
                        response.put("haComprado", false);
                        response.put("entregado", false);
                        return Mono.just(response);
                    }
                })
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", e.getMessage());
                    return Mono.just(errorResponse);
                });
    }
}
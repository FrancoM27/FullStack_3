package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
    name = "servicio-productos",
    url = "http://18.205.233.123:8085/api/productos"
)
public interface ProductoClient {

    @GetMapping("/{id}")
    Map<String, Object> obtenerProducto(@PathVariable("id") Long id);

    @GetMapping
    Map<String, Object>[] obtenerProductos();

    @GetMapping("/vendedor/{vendedorId}")
    Map<String, Object>[] obtenerProductosPorVendedor(@PathVariable("vendedorId") Long vendedorId);

    @PutMapping("/{id}/restar-stock")
    void restarStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}

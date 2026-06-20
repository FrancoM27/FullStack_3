package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
    name = "servicio-pedidos",
    url = "http://18.205.233.123:8082/api/pedidos"
)
public interface PedidoClient {

    @GetMapping("/mis-pedidos")
    Map<String, Object>[] obtenerMisPedidos();

    @GetMapping("/vendedor/{vendedorId}")
    Map<String, Object>[] obtenerPedidosVendedor(@PathVariable("vendedorId") Long vendedorId);

    @GetMapping("/{id}/estado")
    Map<String, Object> validarCompra(@PathVariable("id") Long clienteId, @RequestParam("productoId") Long productoId);

    @PutMapping("/{id}/estado")
    void cambiarEstado(@PathVariable("id") Long id, @RequestParam("nuevoEstado") String nuevoEstado);
}

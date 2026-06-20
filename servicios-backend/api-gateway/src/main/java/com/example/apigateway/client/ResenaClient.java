package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
    name = "servicio-resenas",
    url = "http://18.205.233.123:8081/api/resenas"
)
public interface ResenaClient {

    @GetMapping("/producto/{productoId}")
    Map<String, Object>[] obtenerResenasProducto(@PathVariable("productoId") Long productoId);

    @GetMapping("/cliente")
    Map<String, Object>[] obtenerResenasCliente();

    @GetMapping("/vendedor/{vendedorId}")
    Map<String, Object>[] obtenerResenasVendedor(@PathVariable("vendedorId") Long vendedorId);

    @PostMapping
    Map<String, Object> crearResena(@RequestBody Map<String, Object> resena);

    @PutMapping("/{resenaId}/responder")
    Map<String, Object> responderResena(@PathVariable("resenaId") Long resenaId, @RequestBody String respuesta);
}

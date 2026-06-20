package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
    name = "servicio-pagos",
    url = "http://18.205.233.123:8086/api/pagos"
)
public interface PagoClient {

    @GetMapping("/carrito/{clienteId}")
    Map<String, Object>[] obtenerCarrito(@PathVariable("clientId") Long clienteId);

    @PostMapping("/carrito/agregar")
    Map<String, Object> agregarAlCarrito(@RequestBody Map<String, Object> item);

    @PostMapping("/iniciar")
    Map<String, Object> iniciarPago(@RequestBody Map<String, Object> solicitud);

    @PostMapping("/iniciar-desde-carrito/{clienteId}")
    Map<String, Object> iniciarPagoDesdeCarrito(@PathVariable("clientId") Long clienteId);

    @PostMapping("/confirmar/{idPago}")
    Map<String, Object> confirmarPago(@PathVariable("idPago") Long idPago);
}

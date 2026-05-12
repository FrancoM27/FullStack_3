package com.gamebakes.servicio_resenas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "servicio-pedidos", url = "http://localhost:8082/api/pedidos")
public interface PedidoClient {

    @GetMapping("/validar-compra")
    boolean validarCompra(
            @RequestParam("clienteId") Long clienteId, 
            @RequestParam("productoId") Long productoId
    );
    
}

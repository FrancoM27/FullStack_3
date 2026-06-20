package com.example.apigateway.controller;

import com.example.apigateway.client.PedidoClient;
import com.example.apigateway.client.ProductoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bff/productos")
public class BffProductoController {

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private PedidoClient pedidoClient;

    @GetMapping("/{id}/detalle-completo")
    public Map<String, Object> obtenerProductoDetalleCompleto(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Obtener información del producto
        Map<String, Object> producto = productoClient.obtenerProducto(id);
        response.put("producto", producto);
        
        // Si hay usuario autenticado, verificar si ha comprado el producto
        if (userId != null && authHeader != null) {
            try {
                Map<String, Object>[] pedidos = pedidoClient.obtenerMisPedidos();
                boolean haComprado = false;
                boolean entregado = false;
                
                for (Map<String, Object> pedido : pedidos) {
                    if (pedido.get("productoId") != null && 
                        pedido.get("productoId").equals(id) && 
                        "ENTREGADO".equals(pedido.get("estado"))) {
                        haComprado = true;
                        entregado = true;
                        break;
                    }
                }
                
                response.put("haComprado", haComprado);
                response.put("entregado", entregado);
            } catch (Exception e) {
                response.put("haComprado", false);
                response.put("entregado", false);
            }
        } else {
            response.put("haComprado", false);
            response.put("entregado", false);
        }
        
        return response;
    }
}

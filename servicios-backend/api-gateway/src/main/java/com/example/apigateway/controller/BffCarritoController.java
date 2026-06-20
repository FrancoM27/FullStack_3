package com.example.apigateway.controller;

import com.example.apigateway.client.PagoClient;
import com.example.apigateway.client.ProductoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bff/carrito")
public class BffCarritoController {

    @Autowired
    private PagoClient pagoClient;

    @Autowired
    private ProductoClient productoClient;

    @GetMapping("/completo/{clienteId}")
    public Map<String, Object> obtenerCarritoCompleto(@PathVariable Long clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Obtener items del carrito
            Map<String, Object>[] itemsCarrito = pagoClient.obtenerCarrito(clienteId);
            
            // Agregar detalles de productos a cada item
            List<Map<String, Object>> itemsConDetalles = new ArrayList<>();
            for (Map<String, Object> item : itemsCarrito) {
                Map<String, Object> itemConDetalle = new HashMap<>(item);
                
                try {
                    Long productoId = ((Number) item.get("productoId")).longValue();
                    Map<String, Object> producto = productoClient.obtenerProducto(productoId);
                    itemConDetalle.put("productoDetalle", producto);
                } catch (Exception e) {
                    itemConDetalle.put("productoDetalle", null);
                }
                
                itemsConDetalles.add(itemConDetalle);
            }
            
            response.put("items", itemsConDetalles);
            response.put("total", calcularTotal(itemsConDetalles));
            
        } catch (Exception e) {
            response.put("items", new ArrayList<>());
            response.put("total", 0.0);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
    private double calcularTotal(List<Map<String, Object>> items) {
        double total = 0.0;
        for (Map<String, Object> item : items) {
            Number cantidad = (Number) item.get("cantidad");
            Number precio = (Number) item.get("precioUnitario");
            if (cantidad != null && precio != null) {
                total += cantidad.doubleValue() * precio.doubleValue();
            }
        }
        return total;
    }
}

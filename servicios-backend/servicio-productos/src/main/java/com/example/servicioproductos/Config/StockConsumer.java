package com.example.servicioproductos.Config;

import com.example.servicioproductos.Service.ProductoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockConsumer {

    @Autowired
    private ProductoService productoService;

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "servicio-productos-group")
    public void escucharPago(String mensaje) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(mensaje);

            Long productoId = json.get("productoId").asLong();
            int cantidad = json.get("cantidad").asInt();

            System.out.println("Bajando stock de producto " + productoId + " en " + cantidad);
            productoService.restarStock(productoId, cantidad);

        } catch (Exception e) {
            System.err.println("Error fatal bajando stock desde Kafka: " + e.getMessage());
        }
    }
}
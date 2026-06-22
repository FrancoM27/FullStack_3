package com.example.servicioproductos.Config;

import com.example.servicioproductos.Service.ProductoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
public class StockConsumer {

    @Autowired
    private ProductoService productoService;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "servicio-productos-group")
    public void escucharPago(String mensaje) {
        try {
            JsonNode json = mapper.readTree(mensaje);

            if (json.has("items") && json.get("items").isArray() && !json.get("items").isEmpty()) {
                for (JsonNode item : json.get("items")) {
                    if (item.has("productoId") && item.has("cantidad")) {
                        Long prodId = item.get("productoId").asLong();
                        int cant = item.get("cantidad").asInt();
                        System.out.println("Bajando stock de item múltiple -> productoId: " + prodId + " en " + cant);
                        productoService.restarStock(prodId, cant);
                    }
                }
            }
            else if (json.has("productoId") && json.has("cantidad")) {
                Long prodId = json.get("productoId").asLong();
                int cant = json.get("cantidad").asInt();
                System.out.println("Bajando stock de producto individual -> productoId: " + prodId + " en " + cant);
                productoService.restarStock(prodId, cant);
            } else {
                System.err.println("El mensaje de Kafka no tiene productos válidos para descontar stock.");
            }

        } catch (Exception e) {
            System.err.println("Error fatal bajando stock desde Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
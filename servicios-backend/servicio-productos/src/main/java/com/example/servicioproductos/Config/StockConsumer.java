package com.example.servicioproductos.Config;

import com.example.servicioproductos.Service.ProductoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka; // <-- IMPORTANTE
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka // <-- EL BOTÓN DE ENCENDIDO
@Component
public class StockConsumer {

    @Autowired
    private ProductoService productoService;

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "servicio-productos-group")
    public void escucharPago(String mensaje) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(mensaje);

            // IMPORTANTE: Asegúrate de que los nombres coincidan con los del Map de Pagos
            Long productoId = json.has("productoId") ? json.get("productoId").asLong() : null;
            int cantidad = json.has("cantidad") ? json.get("cantidad").asInt() : 0;

            if (productoId != null) {
                System.out.println("Bajando stock de producto " + productoId + " en " + cantidad);
                productoService.restarStock(productoId, cantidad);
            } else {
                System.out.println("Es un carrito completo, la lógica de bajar stock múltiple va aquí.");
            }

        } catch (Exception e) {
            System.err.println("Error fatal bajando stock desde Kafka: " + e.getMessage());
        }
    }
}
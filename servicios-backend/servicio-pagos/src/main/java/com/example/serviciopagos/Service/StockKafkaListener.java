package com.example.serviciopagos.Service;

import com.example.serviciopagos.Model.ProductoStockCache;
import com.example.serviciopagos.Repository.ProductoStockCacheRepository;
import com.fasterxml.jackson.databind.ObjectMapper; // ESTE ES EL IMPORT CORRECTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StockKafkaListener {

    @Autowired
    private ProductoStockCacheRepository stockCacheRepository;

    @KafkaListener(topics = "topic-stock-productos", groupId = "pagos-group-v2")
    public void escucharCambiosStock(String mensajeJson){
        System.out.println("====== 🚨 MENSAJE DE KAFKA RECIBIDO EN PAGOS 🚨 ======");
        System.out.println("Contenido: " + mensajeJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> evento = mapper.readValue(mensajeJson, Map.class);

            Long productoid = Long.valueOf(evento.get("productoId").toString());
            Integer stock = Integer.valueOf(evento.get("stock").toString());

            ProductoStockCache cache = new ProductoStockCache(productoid, stock);
            stockCacheRepository.save(cache);

            System.out.println("✅ Stock guardado en la BD de Pagos: ID " + productoid + " -> " + stock);
        } catch (Exception e) {
            System.out.println("❌ ERROR CRITICO EN EL LISTENER: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
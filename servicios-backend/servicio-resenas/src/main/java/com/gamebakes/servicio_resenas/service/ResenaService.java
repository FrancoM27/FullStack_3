package com.gamebakes.servicio_resenas.service;

import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.repository.ResenaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate; // Descomenta cuando agreguemos la dependencia
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    //@Autowired
    //private KafkaTemplate<String, String> kafkaTemplate; 

    //CLIENTE: Obtener reseñas por producto con Circuit Breaker
    @CircuitBreaker(name = "resenaCB", fallbackMethod = "fallbackResenas")
    public List<Resena> obtenerPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    //VENDEDOR: Obtener sus reseñas con Circuit Breaker
    @CircuitBreaker(name = "resenaCB", fallbackMethod = "fallbackResenasVendedor")
    public List<Resena> obtenerPorVendedor(Long vendedorId) {
        return resenaRepository.findByVendedorId(vendedorId);
    }

    //Lógica para guardar reseña (Aquí activaremos Kafka después)
    public Resena guardarResena(Resena resena) {
        Resena nueva = resenaRepository.save(resena);
        //kafkaTemplate.send("notificaciones-resenas", "Nueva reseña para el producto: " + resena.getProductoId());
        return nueva;
    }

    //VENDEDOR: Responder a reseña
    public Resena responderResena(Long resenaId, String respuesta) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        resena.setRespuestaVendedor(respuesta);
        return resenaRepository.save(resena);
    }

    //MÉTODOS FALLBACK (Resiliencia)
    public List<Resena> fallbackResenas(Long id, Throwable t) {
        System.out.println("Circuit Breaker: Error obteniendo reseñas del producto. Motivo: " + t.getMessage());
        return new ArrayList<>();
    }

    public List<Resena> fallbackResenasVendedor(Long id, Throwable t) {
        System.out.println("Circuit Breaker: Error obteniendo reseñas del vendedor. Motivo: " + t.getMessage());
        return new ArrayList<>();
    }
}
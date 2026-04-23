package com.gamebakes.servicio_resenas.service;

import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.repository.ResenaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    // Si la DB falla, el "fallback" devolverá una lista vacía en lugar de un error 500
    @CircuitBreaker(name = "resenaCB", fallbackMethod = "fallbackResenas")
    public List<Resena> obtenerPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    public List<Resena> fallbackResenas(Long productoId, Throwable t) {
        System.out.println("Capa de resiliencia activada: " + t.getMessage());
        return new ArrayList<>(); // Retorna lista vacía para que la página no se rompa
    }

    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }
}
package com.example.servicioproductos.Service;

import com.example.servicioproductos.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoService extends JpaRepository<Producto, Long> {
    List<Producto> fidnByCategoria(String categoria);
    List<Producto> findByActivoTrue();
}

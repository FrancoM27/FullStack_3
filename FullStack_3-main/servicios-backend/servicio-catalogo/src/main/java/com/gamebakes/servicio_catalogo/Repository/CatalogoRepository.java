package com.gamebakes.servicio_catalogo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamebakes.servicio_catalogo.Model.Catalogo;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    List<Catalogo> findByCategoriaProducto(String categoriaProducto);
}

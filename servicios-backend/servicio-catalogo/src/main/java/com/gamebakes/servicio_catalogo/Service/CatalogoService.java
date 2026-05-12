package com.gamebakes.servicio_catalogo.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gamebakes.servicio_catalogo.Model.Catalogo;
import com.gamebakes.servicio_catalogo.Repository.CatalogoRepository;

@Service
public class CatalogoService {
    
    @Autowired
    private CatalogoRepository catalogoRepository;

    public List<Catalogo> verCatalogoCompleto() {
        return catalogoRepository.findAll();
    }

    public List<Catalogo> verCatalogoPorCategoria(String categoria) {
        return catalogoRepository.findByCategoriaProducto(categoria);
    }

    public Catalogo verCatalogoPorId(Long id) {
        return catalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el catálogo"));
    }
}

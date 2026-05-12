package com.example.servicioproductos.Service;

import com.example.servicioproductos.Model.Producto;
import com.example.servicioproductos.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar(){
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarPorVendedor(Long vendedorId) {
        return productoRepository.findByVendedorIdAndActivoTrue(vendedorId);
    }

    public Producto guardar(Producto producto){
        if (producto.getPrecio() < 0) throw new RuntimeException("El precio no puede ser negativo");
        if (producto.getStock() < 0) throw new RuntimeException("El stock no puede ser negativo");
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        return productoRepository.save(producto);
    }

    public Producto obtenerPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void eliminar(Long id){
        Producto p = obtenerPorId(id);
        p.setActivo(false);
        productoRepository.save(p);
    }

    public Producto cambiarEstadoActivo(Long id, boolean estado){
        Producto p = obtenerPorId(id);
        p.setActivo(estado);
        return productoRepository.save(p);
    }

    public Producto restarStock(Long id, int cantidad) {
        Producto producto = obtenerPorId(id);
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente en bodega para el producto: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        return productoRepository.save(producto);
    }
}

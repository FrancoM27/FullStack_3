package com.example.servicioproductos.Controller;

import org.springframework.security.core.Authentication;
import com.example.servicioproductos.Model.Producto;
import com.example.servicioproductos.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar(){
        return productoService.listar();
    }

    @GetMapping("/vendedor/{vendedorId}")
    public List<Producto> listarPorVendedor(@PathVariable Long vendedorId){
        return productoService.listarPorVendedor(vendedorId);
    }


    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto, Authentication authentication) {
        Long vendedorId = Long.parseLong(authentication.getName());
        producto.setVendedorId(vendedorId);

        return ResponseEntity.ok(productoService.guardar(producto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId (@PathVariable Long id){
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Producto> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo){
        return ResponseEntity.ok(productoService.cambiarEstadoActivo(id, activo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto, Authentication authentication) {
        Long vendedorId = Long.parseLong(authentication.getName());
        producto.setId(id);
        producto.setVendedorId(vendedorId);

        return ResponseEntity.ok(productoService.guardar(producto));
    }
}

package com.gamebakes.servicio_resenas.controller;

import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class ResenaController {
    @Autowired
    private ResenaService resenaService;

    // --- ENDPOINTS PARA EL CLIENTE ---

    //Permite a cualquier usuario (o cliente logueado) ver las reseñas y calificación de un producto específico.
    @GetMapping("/producto/{id}")
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Long id) {
        List<Resena> resenas = resenaService.obtenerPorProducto(id);
        return ResponseEntity.ok(resenas);
    }

    //Permite a un cliente logueado publicar una reseña
    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        Resena nuevaResena = resenaService.guardarResena(resena);
        return ResponseEntity.ok(nuevaResena);
    }


    // --- ENDPOINTS PARA EL VENDEDOR ---

    //Permite al vendedor visualizar los comentarios y calificaciones de TODOS sus productos
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Resena>> listarPorVendedor(@PathVariable Long vendedorId) {
        List<Resena> resenas = resenaService.obtenerPorVendedor(vendedorId);
        return ResponseEntity.ok(resenas);
    }

    //Permite al vendedor responder a una reseña específica
    @PutMapping("/{id}/responder")
    public ResponseEntity<Resena> responder(@PathVariable Long id, @RequestBody String respuesta) {
        //Limpiamos posibles comillas extras si el front envía texto plano dentro de un JSON
        String textoLimpio = respuesta.replace("\"", "");
        Resena resenaActualizada = resenaService.responderResena(id, textoLimpio);
        return ResponseEntity.ok(resenaActualizada);
    }
}
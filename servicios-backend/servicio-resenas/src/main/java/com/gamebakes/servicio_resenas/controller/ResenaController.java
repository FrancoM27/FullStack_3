package com.gamebakes.servicio_resenas.controller;

import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*") 
public class ResenaController {
    @Autowired
    private ResenaService resenaService;

    @GetMapping("/producto/{id}")
    public List<Resena> listar(@PathVariable Long id) {
        return resenaService.obtenerPorProducto(id);
    }

    @PostMapping
    public Resena crear(@RequestBody Resena resena) {
        return resenaService.guardarResena(resena);
    }
}
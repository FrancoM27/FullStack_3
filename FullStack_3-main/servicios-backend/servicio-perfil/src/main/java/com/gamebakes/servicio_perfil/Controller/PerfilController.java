package com.gamebakes.servicio_perfil.Controller;

import com.gamebakes.servicio_perfil.DTO.PerfilDTO;
import com.gamebakes.servicio_perfil.Service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {
    
    @Autowired
    private PerfilService perfilService;
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PerfilDTO> obtenerPerfil(@PathVariable Long usuarioId) {
        return perfilService.obtenerPerfilPorUsuarioId(usuarioId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PerfilDTO> crearPerfil(@RequestBody PerfilDTO perfilDTO) {
        PerfilDTO nuevoPerfil = perfilService.crearPerfil(perfilDTO);
        return ResponseEntity.ok(nuevoPerfil);
    }
    
    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<PerfilDTO> actualizarPerfil(
            @PathVariable Long usuarioId,
            @RequestBody PerfilDTO perfilDTO) {
        return perfilService.actualizarPerfil(usuarioId, perfilDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

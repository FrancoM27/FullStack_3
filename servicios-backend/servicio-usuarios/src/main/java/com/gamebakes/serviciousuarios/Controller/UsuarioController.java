package com.gamebakes.serviciousuarios.Controller;

import com.gamebakes.serviciousuarios.DTO.LoginDTO;
import com.gamebakes.serviciousuarios.DTO.RegistroDTO;
import com.gamebakes.serviciousuarios.DTO.UsuarioDTO;
import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(@PathVariable Long id,
                                                   @Valid @RequestBody UsuarioDTO dto) {
        
        Usuario usuarioActualizado = usuarioService.actualizarPerfil(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUusario(@Valid @RequestBody RegistroDTO dto) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto) {
        String token = usuarioService.loginUsuario(dto);
        return ResponseEntity.ok(token);
    }
}

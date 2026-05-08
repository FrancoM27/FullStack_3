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

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
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

    @PostMapping("/recuperacion/solicitar")
    public ResponseEntity<Map<String, String>> solicitarRecuperacion(@RequestBody Map<String, String> request){
        String email = request.get("email");
        usuarioService.solicitarRecuperacion(email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Se ha enviado un enlace de recuperación a tu correo.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperacion/confirmar")
    public ResponseEntity<Map<String, String>> confirmarRecuperacion(@RequestBody Map<String, String> request){
        String token = request.get("token");
        String nuevaPassword = request.get("password");

        usuarioService.completarRecuperacion(token, nuevaPassword);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña actualizada con éxito. Ya puedes iniciar sesión");
        return ResponseEntity.ok(response);
    }
}

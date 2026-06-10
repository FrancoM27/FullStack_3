package com.gamebakes.serviciousuarios.Service;

import com.gamebakes.serviciousuarios.DTO.LoginDTO;
import com.gamebakes.serviciousuarios.DTO.RegistroDTO;
import com.gamebakes.serviciousuarios.DTO.UsuarioDTO;
import com.gamebakes.serviciousuarios.Model.PasswordResetToken;
import com.gamebakes.serviciousuarios.Model.Rol;
import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Repository.PasswordResetTokenRepository;
import com.gamebakes.serviciousuarios.Repository.UsuarioRepository;
import com.gamebakes.serviciousuarios.Security.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;



    public Usuario actualizarPerfil(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new RuntimeException("El email ya está en uso");
            }
        });

        usuarioRepository.findByUsername(dto.getUsername()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
        });

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarUsuario(RegistroDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este correo ya tiene una cuenta");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombreCompleto(dto.getNombreCompleto());
        
        try {
            if (dto.getRol() != null) {
                nuevoUsuario.setRol(Rol.valueOf(dto.getRol().toUpperCase()));
            } else {
                nuevoUsuario.setRol(Rol.CLIENTE);
            }
        } catch (Exception e) {
            nuevoUsuario.setRol(Rol.CLIENTE);
        }

        String passwordEncriptada = passwordEncoder.encode(dto.getPassword());
        nuevoUsuario.setPassword(passwordEncriptada);

        return usuarioRepository.save(nuevoUsuario);
    }

    public String loginUsuario(LoginDTO dto) {
        String identificador = dto.getIdentifier(); 

        Usuario usuario = usuarioRepository.findByUsernameOrEmail(identificador, identificador)
                .orElseThrow(() -> new RuntimeException("Usuario o email no encontrado"));


        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        return jwtUtils.generarToken(usuario);
    }

    @Transactional
    public void solicitarRecuperacion(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo electrónico"));

        tokenRepository.deleteByUsuario(usuario);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, usuario);
        tokenRepository.save(resetToken);

        emailService.enviarCorreoRecuperacion(usuario.getEmail(), token);
    }

    @Transactional
    public void completarRecuperacion(String token, String nuevaPassword){
        PasswordResetToken tokenData = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de recuperación no válido o inexistente"));

        if (tokenData.getFechaExpiracion().isBefore(java.time.LocalDateTime.now())){
            tokenRepository.delete(tokenData);
            throw new RuntimeException("El link de recuperación ha expirado");
        }

        Usuario usuario = tokenData.getUsuario();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        tokenRepository.delete(tokenData);
    }
}

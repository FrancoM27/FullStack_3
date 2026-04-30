package com.gamebakes.serviciousuarios.Service;

import com.gamebakes.serviciousuarios.DTO.LoginDTO;
import com.gamebakes.serviciousuarios.DTO.RegistroDTO;
import com.gamebakes.serviciousuarios.DTO.UsuarioDTO;
import com.gamebakes.serviciousuarios.Model.Rol;
import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Repository.UsuarioRepository;
import com.gamebakes.serviciousuarios.Security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public Usuario actualizarPerfil(Long id, UsuarioDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new RuntimeException("El email ya esta en uso por otro usuario");
            }
        });

        usuarioRepository.findByUsername(dto.getUsername()).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new RuntimeException("El nombre de usuario ya está en uso por otro usuario");
            }
        });

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarUsuario(RegistroDTO dto){
        if (usuarioRepository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Este correo ya tiene una cuenta");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombreCompleto(dto.getNombreCompleto());
        nuevoUsuario.setRol(Rol.CLIENTE);

        String passwordEncriptada = passwordEncoder.encode(dto.getPassword());
        nuevoUsuario.setPassword(passwordEncriptada);

        return usuarioRepository.save(nuevoUsuario);
    }

    public String loginUsuario(LoginDTO dto){
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(dto.getIdentifier(), dto.getIdentifier())
                .orElseThrow(() -> new RuntimeException("Usuario o email no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())){
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        return jwtUtils.generarToken(usuario);
    }
}

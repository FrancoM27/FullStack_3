package com.gamebakes.serviciousuarios.Service;

import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario actualizarPerfil(Long id, Usuario datosNuevos){
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreCompleto(datosNuevos.getNombreCompleto());
            usuario.setEmail(datosNuevos.getEmail());

            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

package com.gamebakes.servicio_perfil.Service;

import com.gamebakes.servicio_perfil.DTO.PerfilDTO;
import com.gamebakes.servicio_perfil.Model.Perfil;
import com.gamebakes.servicio_perfil.Model.Usuario;
import com.gamebakes.servicio_perfil.Repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PerfilService {
    
    @Autowired
    private PerfilRepository perfilRepository;
    
    @Transactional(readOnly = true)
    public Optional<PerfilDTO> obtenerPerfilPorUsuarioId(Long usuarioId) {
        return perfilRepository.findByUsuarioId(usuarioId)
            .map(this::convertirADTO);
    }
    
    @Transactional
    public PerfilDTO crearPerfil(PerfilDTO perfilDTO) {
        Perfil perfil = new Perfil();
        
        Usuario usuario = new Usuario();
        usuario.setId(perfilDTO.getUsuarioId());
        perfil.setUsuario(usuario);
        
        perfil.setNombreCompleto(perfilDTO.getNombreCompleto());
        perfil.setTelefono(perfilDTO.getTelefono());
        perfil.setDireccion(perfilDTO.getDireccion());
        
        Perfil perfilGuardado = perfilRepository.save(perfil);
        return convertirADTO(perfilGuardado);
    }
    
    @Transactional
    public Optional<PerfilDTO> actualizarPerfil(Long usuarioId, PerfilDTO perfilDTO) {
        return perfilRepository.findByUsuarioId(usuarioId)
            .map(perfil -> {
                perfil.setNombreCompleto(perfilDTO.getNombreCompleto());
                perfil.setTelefono(perfilDTO.getTelefono());
                perfil.setDireccion(perfilDTO.getDireccion());
                return convertirADTO(perfilRepository.save(perfil));
            });
    }
    
    private PerfilDTO convertirADTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setIdPerfil(perfil.getIdPerfil());
        dto.setNombreCompleto(perfil.getNombreCompleto());
        dto.setTelefono(perfil.getTelefono());
        dto.setDireccion(perfil.getDireccion());
        
        if (perfil.getUsuario() != null) {
            dto.setUsuarioId(perfil.getUsuario().getId());
            dto.setUsername(perfil.getUsuario().getUsername());
            dto.setEmail(perfil.getUsuario().getEmail());
        }
        
        return dto;
    }
}

package com.gamebakes.servicio_perfil.Service;

import com.gamebakes.servicio_perfil.DTO.PerfilDTO;
import com.gamebakes.servicio_perfil.Model.Perfil;
import com.gamebakes.servicio_perfil.Repository.PerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Perfil perfil;
    private PerfilDTO perfilDTO;

    @BeforeEach
    void setUp() {
        perfil = new Perfil();
        perfil.setIdPerfil(1L);
        perfil.setUsuarioId(100L);
        perfil.setNombreCompleto("Juan Pérez");
        perfil.setTelefono("123456789");
        perfil.setDireccion("Calle 123");

        perfilDTO = new PerfilDTO();
        perfilDTO.setIdPerfil(1L);
        perfilDTO.setUsuarioId(100L);
        perfilDTO.setNombreCompleto("Juan Pérez");
        perfilDTO.setTelefono("123456789");
        perfilDTO.setDireccion("Calle 123");
    }

    @Test
    void obtenerPerfilPorUsuarioId_PerfilEncontrado() {
        when(perfilRepository.findByUsuarioId(100L)).thenReturn(Optional.of(perfil));

        Optional<PerfilDTO> result = perfilService.obtenerPerfilPorUsuarioId(100L);

        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getNombreCompleto());
        assertEquals("123456789", result.get().getTelefono());
        assertEquals("Calle 123", result.get().getDireccion());
        verify(perfilRepository, times(1)).findByUsuarioId(100L);
    }

    @Test
    void obtenerPerfilPorUsuarioId_PerfilNoEncontrado() {
        when(perfilRepository.findByUsuarioId(999L)).thenReturn(Optional.empty());

        Optional<PerfilDTO> result = perfilService.obtenerPerfilPorUsuarioId(999L);

        assertFalse(result.isPresent());
        verify(perfilRepository, times(1)).findByUsuarioId(999L);
    }

    @Test
    void crearPerfil_Exito() {
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        PerfilDTO result = perfilService.crearPerfil(perfilDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdPerfil());
        assertEquals(100L, result.getUsuarioId());
        assertEquals("Juan Pérez", result.getNombreCompleto());
        assertEquals("123456789", result.getTelefono());
        assertEquals("Calle 123", result.getDireccion());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void crearPerfil_ConTelefonoNull() {
        perfilDTO.setTelefono(null);
        perfil.setTelefono("");

        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        PerfilDTO result = perfilService.crearPerfil(perfilDTO);

        assertNotNull(result);
        assertEquals("", result.getTelefono());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void crearPerfil_ConDireccionNull() {
        perfilDTO.setDireccion(null);
        perfil.setDireccion("");

        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        PerfilDTO result = perfilService.crearPerfil(perfilDTO);

        assertNotNull(result);
        assertEquals("", result.getDireccion());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void actualizarPerfil_Exito() {
        PerfilDTO perfilActualizado = new PerfilDTO();
        perfilActualizado.setNombreCompleto("Juan Actualizado");
        perfilActualizado.setTelefono("987654321");
        perfilActualizado.setDireccion("Calle 456");

        when(perfilRepository.findByUsuarioId(100L)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        Optional<PerfilDTO> result = perfilService.actualizarPerfil(100L, perfilActualizado);

        assertTrue(result.isPresent());
        verify(perfilRepository, times(1)).findByUsuarioId(100L);
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void actualizarPerfil_PerfilNoEncontrado() {
        PerfilDTO perfilActualizado = new PerfilDTO();
        perfilActualizado.setNombreCompleto("Juan Actualizado");

        when(perfilRepository.findByUsuarioId(999L)).thenReturn(Optional.empty());

        Optional<PerfilDTO> result = perfilService.actualizarPerfil(999L, perfilActualizado);

        assertFalse(result.isPresent());
        verify(perfilRepository, times(1)).findByUsuarioId(999L);
        verify(perfilRepository, never()).save(any(Perfil.class));
    }
}

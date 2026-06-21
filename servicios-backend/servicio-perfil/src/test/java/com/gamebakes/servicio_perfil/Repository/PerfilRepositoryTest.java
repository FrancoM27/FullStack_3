package com.gamebakes.servicio_perfil.Repository;

import com.gamebakes.servicio_perfil.Model.Perfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilRepositoryTest {

    @Mock
    private PerfilRepository perfilRepository;

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil();
        perfil.setIdPerfil(1L);
        perfil.setUsuarioId(100L);
        perfil.setNombreCompleto("Juan Pérez");
        perfil.setTelefono("123456789");
        perfil.setDireccion("Calle 123");
    }

    @Test
    void findByUsuarioId_PerfilEncontrado() {
        when(perfilRepository.findByUsuarioId(100L)).thenReturn(Optional.of(perfil));

        Optional<Perfil> result = perfilRepository.findByUsuarioId(100L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdPerfil());
        assertEquals(100L, result.get().getUsuarioId());
        assertEquals("Juan Pérez", result.get().getNombreCompleto());
        verify(perfilRepository, times(1)).findByUsuarioId(100L);
    }

    @Test
    void findByUsuarioId_PerfilNoEncontrado() {
        when(perfilRepository.findByUsuarioId(999L)).thenReturn(Optional.empty());

        Optional<Perfil> result = perfilRepository.findByUsuarioId(999L);

        assertFalse(result.isPresent());
        verify(perfilRepository, times(1)).findByUsuarioId(999L);
    }

    @Test
    void save_PerfilGuardado() {
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        Perfil result = perfilRepository.save(perfil);

        assertNotNull(result);
        assertEquals(1L, result.getIdPerfil());
        assertEquals(100L, result.getUsuarioId());
        assertEquals("Juan Pérez", result.getNombreCompleto());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void save_PerfilConCamposNull() {
        Perfil perfilConNulls = new Perfil();
        perfilConNulls.setUsuarioId(200L);
        perfilConNulls.setNombreCompleto("María García");
        perfilConNulls.setTelefono(null);
        perfilConNulls.setDireccion(null);

        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfilConNulls);

        Perfil result = perfilRepository.save(perfilConNulls);

        assertNotNull(result);
        assertEquals(200L, result.getUsuarioId());
        assertEquals("María García", result.getNombreCompleto());
        assertNull(result.getTelefono());
        assertNull(result.getDireccion());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }
}

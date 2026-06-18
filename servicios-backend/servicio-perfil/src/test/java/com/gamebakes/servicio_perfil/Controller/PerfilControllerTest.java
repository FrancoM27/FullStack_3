package com.gamebakes.servicio_perfil.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamebakes.servicio_perfil.DTO.PerfilDTO;
import com.gamebakes.servicio_perfil.Service.PerfilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    @Mock
    private PerfilService perfilService;

    @InjectMocks
    private PerfilController perfilController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private PerfilDTO perfilDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(perfilController).build();
        objectMapper = new ObjectMapper();

        perfilDTO = new PerfilDTO();
        perfilDTO.setIdPerfil(1L);
        perfilDTO.setUsuarioId(100L);
        perfilDTO.setNombreCompleto("Juan Pérez");
        perfilDTO.setTelefono("123456789");
        perfilDTO.setDireccion("Calle 123");
    }

    @Test
    void obtenerPerfil_Exito() throws Exception {
        when(perfilService.obtenerPerfilPorUsuarioId(100L)).thenReturn(Optional.of(perfilDTO));

        mockMvc.perform(get("/api/perfil/usuario/{usuarioId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPerfil").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(100L))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$.telefono").value("123456789"))
                .andExpect(jsonPath("$.direccion").value("Calle 123"));

        verify(perfilService, times(1)).obtenerPerfilPorUsuarioId(100L);
    }

    @Test
    void obtenerPerfil_NoEncontrado() throws Exception {
        when(perfilService.obtenerPerfilPorUsuarioId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/perfil/usuario/{usuarioId}", 999L))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).obtenerPerfilPorUsuarioId(999L);
    }

    @Test
    void crearPerfil_Exito() throws Exception {
        when(perfilService.crearPerfil(any(PerfilDTO.class))).thenReturn(perfilDTO);

        mockMvc.perform(post("/api/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPerfil").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(100L))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"));

        verify(perfilService, times(1)).crearPerfil(any(PerfilDTO.class));
    }

    @Test
    void crearPerfil_ConflictoPorDuplicado() throws Exception {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry");
        when(perfilService.crearPerfil(any(PerfilDTO.class))).thenThrow(exception);

        mockMvc.perform(post("/api/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("El perfil ya existe para este usuario"));

        verify(perfilService, times(1)).crearPerfil(any(PerfilDTO.class));
    }

    @Test
    void crearPerfil_ErrorInterno() throws Exception {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Other error");
        when(perfilService.crearPerfil(any(PerfilDTO.class))).thenThrow(exception);

        mockMvc.perform(post("/api/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al crear el perfil"));

        verify(perfilService, times(1)).crearPerfil(any(PerfilDTO.class));
    }

    @Test
    void actualizarPerfil_Exito() throws Exception {
        PerfilDTO perfilActualizado = new PerfilDTO();
        perfilActualizado.setNombreCompleto("Juan Actualizado");
        perfilActualizado.setTelefono("987654321");
        perfilActualizado.setDireccion("Calle 456");

        when(perfilService.actualizarPerfil(eq(100L), any(PerfilDTO.class)))
                .thenReturn(Optional.of(perfilDTO));

        mockMvc.perform(put("/api/perfil/usuario/{usuarioId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilActualizado)))
                .andExpect(status().isOk());

        verify(perfilService, times(1)).actualizarPerfil(eq(100L), any(PerfilDTO.class));
    }

    @Test
    void actualizarPerfil_NoEncontrado() throws Exception {
        PerfilDTO perfilActualizado = new PerfilDTO();
        perfilActualizado.setNombreCompleto("Juan Actualizado");

        when(perfilService.actualizarPerfil(eq(999L), any(PerfilDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/perfil/usuario/{usuarioId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilActualizado)))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).actualizarPerfil(eq(999L), any(PerfilDTO.class));
    }
}

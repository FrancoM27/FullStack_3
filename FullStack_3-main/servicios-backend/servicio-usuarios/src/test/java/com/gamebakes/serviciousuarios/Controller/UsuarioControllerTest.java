package com.gamebakes.serviciousuarios.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamebakes.serviciousuarios.DTO.LoginDTO;
import com.gamebakes.serviciousuarios.DTO.RegistroDTO;
import com.gamebakes.serviciousuarios.DTO.UsuarioDTO;
import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Model.Rol;
import com.gamebakes.serviciousuarios.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Usuario usuario;
    private RegistroDTO registroDTO;
    private LoginDTO loginDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setRol(Rol.CLIENTE);

        registroDTO = new RegistroDTO();
        registroDTO.setUsername("testuser");
        registroDTO.setPassword("password123");
        registroDTO.setEmail("test@example.com");
        registroDTO.setNombreCompleto("Test User");
        registroDTO.setRol("CLIENTE");

        loginDTO = new LoginDTO();
        loginDTO.setIdentifier("testuser");
        loginDTO.setPassword("password123");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("updateduser");
        usuarioDTO.setEmail("updated@example.com");
        usuarioDTO.setNombreCompleto("Updated User");
    }

    @Test
    void actualizarPerfil_Exito() throws Exception {
        when(usuarioService.actualizarPerfil(eq(1L), any(UsuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(usuarioService, times(1)).actualizarPerfil(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    void registrar_Exito() throws Exception {
        when(usuarioService.registrarUsuario(any(RegistroDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nombreCompleto").value("Test User"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));

        verify(usuarioService, times(1)).registrarUsuario(any(RegistroDTO.class));
    }

    @Test
    void login_Exito() throws Exception {
        when(usuarioService.loginUsuario(any(LoginDTO.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));

        verify(usuarioService, times(1)).loginUsuario(any(LoginDTO.class));
    }

    @Test
    void solicitarRecuperacion_Exito() throws Exception {
        doNothing().when(usuarioService).solicitarRecuperacion(anyString());

        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        mockMvc.perform(post("/api/usuarios/recuperacion/solicitar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Se ha enviado un enlace de recuperación a tu correo."));

        verify(usuarioService, times(1)).solicitarRecuperacion("test@example.com");
    }

    @Test
    void confirmarRecuperacion_Exito() throws Exception {
        doNothing().when(usuarioService).completarRecuperacion(anyString(), anyString());

        Map<String, String> request = new HashMap<>();
        request.put("token", "token123");
        request.put("password", "newPassword");

        mockMvc.perform(post("/api/usuarios/recuperacion/confirmar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contraseña actualizada con éxito. Ya puedes iniciar sesión"));

        verify(usuarioService, times(1)).completarRecuperacion("token123", "newPassword");
    }
}

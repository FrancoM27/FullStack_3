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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private RegistroDTO registroDTO;
    private LoginDTO loginDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
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
    void actualizarPerfil_Exito() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByUsername("updateduser")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.actualizarPerfil(1L, usuarioDTO);

        assertNotNull(result);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarPerfil_UsuarioNoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarPerfil(999L, usuarioDTO);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarPerfil_EmailDuplicado() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setEmail("updated@example.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("updated@example.com")).thenReturn(Optional.of(otroUsuario));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarPerfil(1L, usuarioDTO);
        });

        assertEquals("El email ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarPerfil_UsernameDuplicado() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setUsername("updateduser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByUsername("updateduser")).thenReturn(Optional.of(otroUsuario));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarPerfil(1L, usuarioDTO);
        });

        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_Exito() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrarUsuario(registroDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_UsernameDuplicado() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(registroDTO);
        });

        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_EmailDuplicado() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(registroDTO);
        });

        assertEquals("Este correo ya tiene una cuenta", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, times(1)).existsByEmail("test@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_RolInvalido_DefaultCliente() {
        registroDTO.setRol("INVALIDO");
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrarUsuario(registroDTO);

        assertNotNull(result);
        assertEquals(Rol.CLIENTE, result.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_SinRol_DefaultCliente() {
        registroDTO.setRol(null);
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrarUsuario(registroDTO);

        assertNotNull(result);
        assertEquals(Rol.CLIENTE, result.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void loginUsuario_Exito() {
        when(usuarioRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generarToken(usuario)).thenReturn("jwt-token");

        String result = usuarioService.loginUsuario(loginDTO);

        assertEquals("jwt-token", result);
        verify(usuarioRepository, times(1)).findByUsernameOrEmail("testuser", "testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtils, times(1)).generarToken(usuario);
    }

    @Test
    void loginUsuario_UsuarioNoEncontrado() {
        loginDTO.setIdentifier("nonexistent");
        when(usuarioRepository.findByUsernameOrEmail("nonexistent", "nonexistent")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.loginUsuario(loginDTO);
        });

        assertEquals("Usuario o email no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByUsernameOrEmail("nonexistent", "nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginUsuario_ContrasenaIncorrecta() {
        when(usuarioRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.loginUsuario(loginDTO);
        });

        assertEquals("Usuario o contraseña incorrectos", exception.getMessage());
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtils, never()).generarToken(any(Usuario.class));
    }

    @Test
    void solicitarRecuperacion_Exito() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        doNothing().when(tokenRepository).deleteByUsuario(usuario);
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
        doNothing().when(emailService).enviarCorreoRecuperacion(anyString(), anyString());

        usuarioService.solicitarRecuperacion("test@example.com");

        verify(usuarioRepository, times(1)).findByEmail("test@example.com");
        verify(tokenRepository, times(1)).deleteByUsuario(usuario);
        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).enviarCorreoRecuperacion(eq("test@example.com"), anyString());
    }

    @Test
    void solicitarRecuperacion_EmailNoEncontrado() {
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.solicitarRecuperacion("nonexistent@example.com");
        });

        assertEquals("No existe un usuario con ese correo electrónico", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("nonexistent@example.com");
        verify(tokenRepository, never()).deleteByUsuario(any(Usuario.class));
    }

    @Test
    void completarRecuperacion_Exito() {
        PasswordResetToken token = new PasswordResetToken("token123", usuario);
        when(tokenRepository.findByToken("token123")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        doNothing().when(tokenRepository).delete(token);

        usuarioService.completarRecuperacion("token123", "newPassword");

        verify(tokenRepository, times(1)).findByToken("token123");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    void completarRecuperacion_TokenNoValido() {
        when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.completarRecuperacion("invalidToken", "newPassword");
        });

        assertEquals("Token de recuperación no válido o inexistente", exception.getMessage());
        verify(tokenRepository, times(1)).findByToken("invalidToken");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}

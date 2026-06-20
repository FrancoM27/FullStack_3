package com.gamebakes.serviciousuarios.Repository;

import com.gamebakes.serviciousuarios.Model.Usuario;
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
class UsuarioRepositoryTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("password123");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setRol(com.gamebakes.serviciousuarios.Model.Rol.CLIENTE);
    }

    @Test
    void findByUsername_UsuarioEncontrado() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void findByUsername_UsuarioNoEncontrado() {
        when(usuarioRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void findByEmail_UsuarioEncontrado() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_UsuarioNoEncontrado() {
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioRepository.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void findByUsernameOrEmail_UsuarioEncontrado() {
        when(usuarioRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioRepository.findByUsernameOrEmail("testuser", "testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(usuarioRepository, times(1)).findByUsernameOrEmail("testuser", "testuser");
    }

    @Test
    void findByUsernameOrEmail_UsuarioNoEncontrado() {
        when(usuarioRepository.findByUsernameOrEmail("nonexistent", "nonexistent")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioRepository.findByUsernameOrEmail("nonexistent", "nonexistent");

        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findByUsernameOrEmail("nonexistent", "nonexistent");
    }

    @Test
    void existsByUsername_UsernameExiste() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        boolean result = usuarioRepository.existsByUsername("testuser");

        assertTrue(result);
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
    }

    @Test
    void existsByUsername_UsernameNoExiste() {
        when(usuarioRepository.existsByUsername("nonexistent")).thenReturn(false);

        boolean result = usuarioRepository.existsByUsername("nonexistent");

        assertFalse(result);
        verify(usuarioRepository, times(1)).existsByUsername("nonexistent");
    }

    @Test
    void existsByEmail_EmailExiste() {
        when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = usuarioRepository.existsByEmail("test@example.com");

        assertTrue(result);
        verify(usuarioRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void existsByEmail_EmailNoExiste() {
        when(usuarioRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        boolean result = usuarioRepository.existsByEmail("nonexistent@example.com");

        assertFalse(result);
        verify(usuarioRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    void save_UsuarioGuardado() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioRepository.save(usuario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}

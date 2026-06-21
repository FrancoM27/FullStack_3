package com.gamebakes.serviciousuarios.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    void testGettersAndSetters() {
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("password123");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setRol(Rol.CLIENTE);

        assertEquals(1L, usuario.getId());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("Test User", usuario.getNombreCompleto());
        assertEquals(Rol.CLIENTE, usuario.getRol());
    }

    @Test
    void testConstructorVacio() {
        Usuario usuarioVacio = new Usuario();
        assertNotNull(usuarioVacio);
    }

    @Test
    void testConstructorCompleto() {
        Usuario usuarioCompleto = new Usuario(1L, "testuser", "password123", "test@example.com", "Test User", Rol.CLIENTE);

        assertEquals(1L, usuarioCompleto.getId());
        assertEquals("testuser", usuarioCompleto.getUsername());
        assertEquals("password123", usuarioCompleto.getPassword());
        assertEquals("test@example.com", usuarioCompleto.getEmail());
        assertEquals("Test User", usuarioCompleto.getNombreCompleto());
        assertEquals(Rol.CLIENTE, usuarioCompleto.getRol());
    }

    @Test
    void testCamposNulos() {
        usuario.setId(null);
        usuario.setUsername(null);
        usuario.setPassword(null);
        usuario.setEmail(null);
        usuario.setNombreCompleto(null);
        usuario.setRol(null);

        assertNull(usuario.getId());
        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertNull(usuario.getEmail());
        assertNull(usuario.getNombreCompleto());
        assertNull(usuario.getRol());
    }

    @Test
    void testRolVendedor() {
        usuario.setRol(Rol.VENDEDOR);
        assertEquals(Rol.VENDEDOR, usuario.getRol());
    }
}

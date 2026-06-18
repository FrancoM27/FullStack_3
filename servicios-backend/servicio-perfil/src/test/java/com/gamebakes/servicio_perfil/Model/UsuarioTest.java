package com.gamebakes.servicio_perfil.Model;

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
        usuario.setUsername("juanperez");
        usuario.setPassword("password123");
        usuario.setEmail("juan@example.com");
        usuario.setNombreCompleto("Juan Pérez");
        usuario.setRol(Rol.CLIENTE);

        assertEquals(1L, usuario.getId());
        assertEquals("juanperez", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("Juan Pérez", usuario.getNombreCompleto());
        assertEquals(Rol.CLIENTE, usuario.getRol());
    }

    @Test
    void testConstructorVacio() {
        Usuario usuarioVacio = new Usuario();
        assertNotNull(usuarioVacio);
    }

    @Test
    void testConstructorCompleto() {
        Usuario usuarioCompleto = new Usuario(
            1L,
            "juanperez",
            "password123",
            "juan@example.com",
            "Juan Pérez",
            Rol.CLIENTE
        );

        assertEquals(1L, usuarioCompleto.getId());
        assertEquals("juanperez", usuarioCompleto.getUsername());
        assertEquals("password123", usuarioCompleto.getPassword());
        assertEquals("juan@example.com", usuarioCompleto.getEmail());
        assertEquals("Juan Pérez", usuarioCompleto.getNombreCompleto());
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

    @Test
    void testRolAdmin() {
        usuario.setRol(Rol.ADMIN);
        assertEquals(Rol.ADMIN, usuario.getRol());
    }

    @Test
    void testRolUsuario() {
        usuario.setRol(Rol.USUARIO);
        assertEquals(Rol.USUARIO, usuario.getRol());
    }
}

package com.gamebakes.serviciousuarios.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {

    private PasswordResetToken token;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
    }

    @Test
    void testConstructorConParametros() {
        token = new PasswordResetToken("token123", usuario);

        assertEquals("token123", token.getToken());
        assertEquals(usuario, token.getUsuario());
        assertNotNull(token.getFechaExpiracion());
        assertTrue(token.getFechaExpiracion().isAfter(LocalDateTime.now()));
    }

    @Test
    void testConstructorVacio() {
        token = new PasswordResetToken();
        assertNotNull(token);
    }

    @Test
    void testGettersAndSetters() {
        token = new PasswordResetToken();
        token.setId(1L);
        token.setToken("token123");
        token.setUsuario(usuario);
        token.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        assertEquals(1L, token.getId());
        assertEquals("token123", token.getToken());
        assertEquals(usuario, token.getUsuario());
        assertNotNull(token.getFechaExpiracion());
    }

    @Test
    void testFechaExpiracion15Minutos() {
        LocalDateTime antes = LocalDateTime.now();
        token = new PasswordResetToken("token123", usuario);
        LocalDateTime despues = LocalDateTime.now();

        assertTrue(token.getFechaExpiracion().isAfter(antes.plusMinutes(14)));
        assertTrue(token.getFechaExpiracion().isBefore(despues.plusMinutes(16)));
    }

    @Test
    void testCamposNulos() {
        token = new PasswordResetToken();
        token.setId(null);
        token.setToken(null);
        token.setUsuario(null);
        token.setFechaExpiracion(null);

        assertNull(token.getId());
        assertNull(token.getToken());
        assertNull(token.getUsuario());
        assertNull(token.getFechaExpiracion());
    }
}

package com.gamebakes.serviciousuarios.Security;

import com.gamebakes.serviciousuarios.Model.Usuario;
import com.gamebakes.serviciousuarios.Model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setRol(Rol.CLIENTE);
    }

    @Test
    void generarToken_TokenGenerado() {
        String token = jwtUtils.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void getSubjectFromToken_SubjectCorrecto() {
        String token = jwtUtils.generarToken(usuario);
        String subject = jwtUtils.getSubjectFromToken(token);

        assertEquals("1", subject);
    }

    @Test
    void getSubjectFromToken_TokenInvalido() {
        assertThrows(Exception.class, () -> {
            jwtUtils.getSubjectFromToken("invalid.token.here");
        });
    }

    @Test
    void generarToken_TokenContieneClaims() {
        String token = jwtUtils.generarToken(usuario);
        String subject = jwtUtils.getSubjectFromToken(token);

        assertNotNull(subject);
        assertEquals("1", subject);
    }

    @Test
    void generarToken_TokenGeneradoCorrectamente() {
        String token = jwtUtils.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }
}

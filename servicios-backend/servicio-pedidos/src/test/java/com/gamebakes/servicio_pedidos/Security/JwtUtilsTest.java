package com.gamebakes.servicio_pedidos.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String secretKey = "GameBakes_Secret_Key_2026_No_Compartir";
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        
        // Crear un token válido para pruebas
        validToken = Jwts.builder()
                .setSubject("testUser")
                .claim("rol", "CLIENTE")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Test
    void testGetSubjectFromToken_ValidToken_ReturnsSubject() {
        String subject = jwtUtils.getSubjectFromToken(validToken);
        assertEquals("testUser", subject);
    }

    @Test
    void testGetSubjectFromToken_InvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> jwtUtils.getSubjectFromToken(invalidToken));
    }

    @Test
    void testGetRolFromToken_ValidToken_ReturnsRole() {
        String rol = jwtUtils.getRolFromToken(validToken);
        assertEquals("CLIENTE", rol);
    }

    @Test
    void testGetRolFromToken_TokenWithoutRole_ReturnsNull() {
        String tokenWithoutRole = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        
        String rol = jwtUtils.getRolFromToken(tokenWithoutRole);
        assertNull(rol);
    }

    @Test
    void testGetRolFromToken_InvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> jwtUtils.getRolFromToken(invalidToken));
    }

    @Test
    void testGetSubjectFromToken_TokenWithDifferentSubject_ReturnsCorrectSubject() {
        String tokenWithDifferentSubject = Jwts.builder()
                .setSubject("anotherUser")
                .claim("rol", "VENDEDOR")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        
        String subject = jwtUtils.getSubjectFromToken(tokenWithDifferentSubject);
        assertEquals("anotherUser", subject);
    }

    @Test
    void testGetRolFromToken_TokenWithDifferentRole_ReturnsCorrectRole() {
        String tokenWithDifferentRole = Jwts.builder()
                .setSubject("testUser")
                .claim("rol", "VENDEDOR")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        
        String rol = jwtUtils.getRolFromToken(tokenWithDifferentRole);
        assertEquals("VENDEDOR", rol);
    }
}

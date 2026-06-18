package com.gamebakes.servicio_perfil.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private String validToken;
    private String secretKey = "GameBakes_Secret_Key_2026_No_Compartir";

    @BeforeEach
    void setUp() {
        validToken = Jwts.builder()
                .setSubject("testuser")
                .claim("rol", "CLIENTE")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Test
    void getSubjectFromToken_TokenValido() {
        String subject = jwtUtils.getSubjectFromToken(validToken);
        assertEquals("testuser", subject);
    }

    @Test
    void getSubjectFromToken_TokenInvalido() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> {
            jwtUtils.getSubjectFromToken(invalidToken);
        });
    }

    @Test
    void getSubjectFromToken_TokenNull() {
        assertThrows(Exception.class, () -> {
            jwtUtils.getSubjectFromToken(null);
        });
    }

    @Test
    void getRolFromToken_TokenValido() {
        String rol = jwtUtils.getRolFromToken(validToken);
        assertEquals("CLIENTE", rol);
    }

    @Test
    void getRolFromToken_TokenSinRol() {
        String tokenSinRol = Jwts.builder()
                .setSubject("testuser")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        
        String rol = jwtUtils.getRolFromToken(tokenSinRol);
        assertNull(rol);
    }

    @Test
    void getRolFromToken_TokenInvalido() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> {
            jwtUtils.getRolFromToken(invalidToken);
        });
    }

    @Test
    void getRolFromToken_TokenNull() {
        assertThrows(Exception.class, () -> {
            jwtUtils.getRolFromToken(null);
        });
    }

    @Test
    void getClaims_TokenValido() {
        assertNotNull(jwtUtils.getSubjectFromToken(validToken));
        assertNotNull(jwtUtils.getRolFromToken(validToken));
    }

    @Test
    void getClaims_TokenMalformado() {
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid";
        assertThrows(Exception.class, () -> {
            jwtUtils.getSubjectFromToken(malformedToken);
        });
    }

    @Test
    void getClaims_TokenConFirmaIncorrecta() {
        String wrongSecretKey = "Wrong_Secret_Key_Must_Be_At_Least_256_Bits_Long_For_HMAC_SHA";
        String tokenWithWrongSignature = Jwts.builder()
                .setSubject("testuser")
                .claim("rol", "CLIENTE")
                .signWith(Keys.hmacShaKeyFor(wrongSecretKey.getBytes()))
                .compact();
        
        assertThrows(Exception.class, () -> {
            jwtUtils.getSubjectFromToken(tokenWithWrongSignature);
        });
    }
}

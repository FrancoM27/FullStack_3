package com.gamebakes.serviciousuarios.Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Test
    void securityConfig_CreacionExitosa() {
        SecurityConfig securityConfig = new SecurityConfig();
        assertNotNull(securityConfig);
    }

    @Test
    void passwordEncoder_CreacionExitosa() {
        SecurityConfig securityConfig = new SecurityConfig();
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    void passwordEncoder_EncryptaCorrectamente() {
        SecurityConfig securityConfig = new SecurityConfig();
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        
        assertNotNull(encryptedPassword);
        assertNotEquals(password, encryptedPassword);
        assertTrue(passwordEncoder.matches(password, encryptedPassword));
    }

    @Test
    void corsConfigurationSource_CreacionExitosa() {
        SecurityConfig securityConfig = new SecurityConfig();
        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        assertNotNull(corsConfigurationSource);
    }
}

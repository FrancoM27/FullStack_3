package com.gamebakes.servicio_resenas.Config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testSecurityConfig_CanBeInstantiated() {
        SecurityConfig securityConfig = new SecurityConfig();
        assertNotNull(securityConfig);
    }

    @Test
    void testSecurityConfig_IsConfiguration() {
        assertNotNull(SecurityConfig.class.getAnnotation(org.springframework.context.annotation.Configuration.class));
    }
}

package com.example.serviciopagos.Config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    @Test
    void testCorsConfigurationSource() {
        SecurityConfig config = new SecurityConfig();
        ReflectionTestUtils.setField(config, "frontendUrl", "http://test1");
        ReflectionTestUtils.setField(config, "frontendUrlAlt", "http://test2");

        assertNotNull(config.corsConfigurationSource());
    }
}
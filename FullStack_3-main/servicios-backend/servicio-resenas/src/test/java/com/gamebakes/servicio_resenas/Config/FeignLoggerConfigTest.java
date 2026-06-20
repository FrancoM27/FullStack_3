package com.gamebakes.servicio_resenas.Config;

import feign.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeignLoggerConfigTest {

    @Test
    void testFeignLoggerLevel_ReturnsFullLevel() {
        FeignLoggerConfig config = new FeignLoggerConfig();
        Logger.Level level = config.feignLoggerLevel();
        
        assertEquals(Logger.Level.FULL, level);
    }

    @Test
    void testFeignLoggerLevel_NotNull() {
        FeignLoggerConfig config = new FeignLoggerConfig();
        Logger.Level level = config.feignLoggerLevel();
        
        assertNotNull(level);
    }

    @Test
    void testFeignLoggerLevel_ReturnsCorrectEnumValue() {
        FeignLoggerConfig config = new FeignLoggerConfig();
        Logger.Level level = config.feignLoggerLevel();
        
        assertEquals(Logger.Level.FULL.name(), level.name());
    }
}

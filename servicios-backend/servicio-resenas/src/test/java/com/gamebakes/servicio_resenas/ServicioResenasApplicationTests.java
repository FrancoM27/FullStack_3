package com.gamebakes.servicio_resenas;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ServicioResenasApplicationTests {

    @Test
    void contextLoads() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ServicioResenasApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(ServicioResenasApplication.class, new String[]{}));
        }
    }
}
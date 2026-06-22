package com.example.serviciopagos;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ServicioPagosApplicationTests {

    @Test
    void contextLoads() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ServicioPagosApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(ServicioPagosApplication.class, new String[]{}));
        }
    }
}
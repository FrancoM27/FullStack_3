package com.gamebakes.servicio_pedidos;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ServicioPedidosApplicationTests {

    @Test
    void contextLoads() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ServicioPedidosApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(ServicioPedidosApplication.class, new String[]{}));
        }
    }
}
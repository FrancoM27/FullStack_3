package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public WebClient pagosWebClient() {
        return WebClient.builder()
                .baseUrl("http://servicio-pagos:8086/api/pagos")
                .build();
    }

    @Bean
    public WebClient productosWebClient() {
        return WebClient.builder()
                .baseUrl("http://servicio-productos:8085/api/productos")
                .build();
    }

    @Bean
    public WebClient pedidosWebClient() {
        return WebClient.builder()
                .baseUrl("http://servicio-pedidos:8082/api/pedidos")
                .build();
    }

    @Bean
    public WebClient perfilWebClient() {
        return WebClient.builder()
                .baseUrl("http://servicio-perfil:8084/api/perfil")
                .build();
    }

    @Bean
    public WebClient resenasWebClient() {
        return WebClient.builder()
                .baseUrl("http://servicio-resenas:8081/api/resenas")
                .build();
    }
}

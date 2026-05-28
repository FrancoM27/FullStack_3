package com.gamebakes.servicio_resenas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableRetry
public class ServicioResenasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioResenasApplication.class, args);
    }

}
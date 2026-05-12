package com.gamebakes.servicio_resenas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioResenasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioResenasApplication.class, args);
    }

}
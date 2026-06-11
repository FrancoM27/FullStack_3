package com.example.serviciopagos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ServicioPagosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioPagosApplication.class, args);
    }

}

package com.example.servicioproductos.Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerConfigTest {

    @Test
    void kafkaProducerConfig_CreacionExitosa() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        assertNotNull(config);
    }

    @Test
    void producerFactory_ConfiguracionCorrecta() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        ProducerFactory<String, String> producerFactory = config.producerFactory();
        assertNotNull(producerFactory);
    }

    @Test
    void kafkaTemplate_ConfiguracionCorrecta() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        KafkaTemplate<String, String> kafkaTemplate = config.kafkaTemplate();
        assertNotNull(kafkaTemplate);
    }
}

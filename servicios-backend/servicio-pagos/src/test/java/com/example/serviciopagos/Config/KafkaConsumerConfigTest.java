package com.example.serviciopagos.Config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaConsumerConfigTest {

    @Test
    void testBeans() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        ReflectionTestUtils.setField(config, "bootstrapServers", "localhost:9092");

        assertNotNull(config.consumerFactory());
        assertNotNull(config.kafkaListenerContainerFactory());
    }
}
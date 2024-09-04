package io.sensors.central.service;

import io.sensors.central.service.testcontainer.NatsContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SensorsCentralApplicationTests {
    @Container
    static NatsContainer natsContainer = new NatsContainer();

    @DynamicPropertySource
    static void natsProperties(DynamicPropertyRegistry registry) {
        registry.add("warehouse.nats.server.uri", natsContainer::getServerUrl);
        registry.add("warehouse.nats.server.user", natsContainer::getUser);
        registry.add("warehouse.nats.server.password", natsContainer::getPassword);
    }

    @Test
    void contextLoads() {
    }

}

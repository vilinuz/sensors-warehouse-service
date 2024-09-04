package io.sensors.central.service;

import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WarehousePropertiesConfig.class)
public class SensorsCentralApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorsCentralApplication.class, args);
    }

}

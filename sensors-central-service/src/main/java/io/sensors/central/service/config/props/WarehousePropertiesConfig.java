package io.sensors.central.service.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "warehouse")
public record WarehousePropertiesConfig(NatsPropertiesConfig nats, List<SensorPropertiesConfig> sensors) {
}

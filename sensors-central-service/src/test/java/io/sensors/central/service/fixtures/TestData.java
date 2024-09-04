package io.sensors.central.service.fixtures;

import io.sensors.central.service.config.props.NatsPropertiesConfig;
import io.sensors.central.service.config.props.SensorPropertiesConfig;
import io.sensors.central.service.config.props.ServerPropertiesConfig;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestData {

    public WarehousePropertiesConfig createWarehousePropertiesConfig() {
        return new WarehousePropertiesConfig(createNatsConfig(), createSensorPropertiesConfigs());
    }

    public SensorPropertiesConfig createSensorPropertiesConfig(String id, String subject, int port, int threshold) {
        return new SensorPropertiesConfig(id, subject, port, threshold);
    }

    List<SensorPropertiesConfig> createSensorPropertiesConfigs() {
        return List.of(
                createSensorPropertiesConfig("t1", "temperature", 3344, 50),
                createSensorPropertiesConfig("h1", "humidity", 3355, 80));
    }

    public NatsPropertiesConfig createNatsConfig() {
        return new NatsPropertiesConfig(createServerConfig());
    }

    public ServerPropertiesConfig createServerConfig() {
        return new ServerPropertiesConfig("nats://localhost:4222", "user", "password");
    }
}

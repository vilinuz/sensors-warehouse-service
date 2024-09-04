package io.sensors.warehouse.validator;

import io.sensors.warehouse.config.props.SensorPropertiesConfig;
import io.sensors.warehouse.config.props.WarehousePropertiesConfig;
import io.sensors.warehouse.exception.SensorMessageValidationException;
import io.sensors.warehouse.model.SensorMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorMessageValidator {
    private final WarehousePropertiesConfig config;

    public SensorMessageValidator(WarehousePropertiesConfig config) {
        this.config = config;
    }

    public void validate(final SensorMessage message) {
        List<String> allowedSensorIds = config.sensors().stream().map(SensorPropertiesConfig::id).toList();

        if (!allowedSensorIds.contains(message.id())) {
            throw new SensorMessageValidationException(
                    String.format("Message %s has invalid id '%s'", message, message.id()));
        }
    }
}

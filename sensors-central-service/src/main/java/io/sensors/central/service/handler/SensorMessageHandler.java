package io.sensors.central.service.handler;

import io.sensors.central.service.alarm.event.AlarmEvent;
import io.sensors.central.service.alarm.event.AlarmEventPublisher;
import io.sensors.central.service.config.props.SensorPropertiesConfig;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import io.sensors.central.service.model.SensorMessage;
import io.sensors.central.service.model.SensorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SensorMessageHandler {

    private final AlarmEventPublisher alarmEventPublisher;
    private final WarehousePropertiesConfig config;

    public SensorMessageHandler(AlarmEventPublisher alarmEventPublisher, WarehousePropertiesConfig config) {
        this.alarmEventPublisher = alarmEventPublisher;
        this.config = config;
    }

    public void handle(SensorMessage message) {
        Optional<SensorPropertiesConfig> sensorPropertiesConfig = config.sensors().stream()
                .filter(sensor -> sensor.id().equals(message.id()))
                .findAny();

        sensorPropertiesConfig.ifPresent(sensor -> {
            SensorStatus status = resolveSensorStatus(message, sensor);

            switch (status) {
                case NORMAL:
                    handleNormalSensorMessage(message);
                    break;
                case ALARMING:
                    handleAlarmingSensorMessage(message);
            }
        });
    }

    private void handleNormalSensorMessage(SensorMessage sensorMessage) {
        log.debug("Sensor message received: {}", sensorMessage);
    }

    private void handleAlarmingSensorMessage(SensorMessage message) {
        log.warn("Alarming Sensor message received: {}", message);

        AlarmEvent alarmEvent = AlarmEvent.builder()
                .sensorId(message.id())
                .value(Integer.parseInt(message.value()))
                .build();

        alarmEventPublisher.publish(alarmEvent);
    }

    private SensorStatus resolveSensorStatus(final SensorMessage message, final SensorPropertiesConfig sensorConfig) {
        try {
            int value = Integer.parseInt(message.value());

            return isThresholdExceeded(value, sensorConfig) ? SensorStatus.ALARMING : SensorStatus.NORMAL;
        } catch (NumberFormatException e) {
            log.error("Invalid '{}' value received for message Id: '{}'", message.value(), message.id());
            return SensorStatus.NORMAL;
        }
    }

    private boolean isThresholdExceeded(final int value, final SensorPropertiesConfig config) {
        return value >= config.threshold();
    }
}

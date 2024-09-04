package io.sensors.central.service.handler;

import io.sensors.central.service.alarm.event.AlarmEventPublisher;
import io.sensors.central.service.config.props.SensorPropertiesConfig;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import io.sensors.central.service.model.SensorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorMessageHandlerTest {

    @Mock
    private AlarmEventPublisher alarmEventPublisher;

    @Mock
    private WarehousePropertiesConfig config;

    @InjectMocks
    private SensorMessageHandler handler;

    @Test
    void handle_noAlarmTriggered() {
        doNothing().when(alarmEventPublisher).publish(any());
        when(config.sensors()).thenReturn(List.of(
                new SensorPropertiesConfig("t1", "temperature", 3344, 50),
                new SensorPropertiesConfig("t1", "humidity", 3355, 80)
        ));

        handler.handle(new SensorMessage("t1", "51"));

        verify(alarmEventPublisher, times(1)).publish(any());
    }

    @Test
    void handle_alarmTriggered() {
        when(config.sensors()).thenReturn(List.of(
                new SensorPropertiesConfig("t1", "temperature", 3344, 50),
                new SensorPropertiesConfig("t1", "humidity", 3355, 80)
        ));

        handler.handle(new SensorMessage("t1", "49"));

        verify(alarmEventPublisher, never()).publish(any());
    }
}
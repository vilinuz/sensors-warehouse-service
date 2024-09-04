package io.sensors.central.service.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.nats.client.Subscription;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import io.sensors.central.service.fixtures.TestData;
import io.sensors.central.service.handler.SensorMessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorMessageSubscriberTest {
    @Mock
    private SensorMessageHandler messageHandler;

    @Mock
    private Connection connection;

    @Mock
    private WarehousePropertiesConfig warehousePropertiesConfig;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SensorMessageSubscriber subscriber;

    @Test
    void subscribe() {
        Dispatcher dispatcher = mock(Dispatcher.class);
        Subscription subscription = mock(Subscription.class);
        when(connection.createDispatcher()).thenReturn(dispatcher);
        when(dispatcher.subscribe(anyString(), any(MessageHandler.class))).thenReturn(subscription);
        doReturn(TestData.createWarehousePropertiesConfig().sensors()).when(warehousePropertiesConfig).sensors();

        subscriber.subscribe();

        verify(dispatcher, times(2)).subscribe(anyString(), any(MessageHandler.class));
    }
}
package io.sensors.central.service.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import io.sensors.central.service.handler.SensorMessageHandler;
import io.sensors.central.service.model.SensorMessage;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SensorMessageSubscriber {
    private final WarehousePropertiesConfig config;
    private final Connection connection;
    private final SensorMessageHandler messageHandler;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public SensorMessageSubscriber(final WarehousePropertiesConfig config,
                                   final SensorMessageHandler messageHandler,
                                   final Connection connection,
                                   final ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
        this.connection = connection;
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    public void subscribe() {
        Dispatcher dispatcher = connection.createDispatcher();
        config.sensors()
                .forEach(config -> dispatcher.subscribe(config.subject(), handler()));
    }

    public MessageHandler handler() {
        return message -> {
            String data = new String(message.getData());
            log.info("Message Received: '{}'", data);
            try {
                messageHandler.handle(objectMapper.readValue(data, SensorMessage.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to parse sensor message", e);
            }
        };
    }

}

package io.sensors.warehouse.service;

import io.nats.client.Connection;
import io.sensors.warehouse.config.props.SensorPropertiesConfig;
import io.sensors.warehouse.converter.SensorsMessageConverter;
import io.sensors.warehouse.exception.SensorHandlerFailedException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
public class SensorsMessageHandler {

    private final SensorsUdpChannelReceiver receiver;
    private final Connection natsConnection;
    private final SensorsMessageConverter messageConverter;
    @Getter
    private final SensorPropertiesConfig config;

    public SensorsMessageHandler(SensorsUdpChannelReceiver receiver,
                                 Connection natsConnection,
                                 SensorsMessageConverter messageConverter,
                                 SensorPropertiesConfig config) {
        this.receiver = receiver;
        this.natsConnection = natsConnection;
        this.messageConverter = messageConverter;
        this.config = config;
    }

    public void start() throws IOException {
        start(config.udpPort());
    }

    public void start(final int port) throws IOException {
        log.info("Warehouse Server is listening on port: {}", port);
        Selector selector = receiver.createChannel(port);

        while (true) {
            selector.select();
            processMessages(selector);
        }
    }

    private void processMessages(Selector selector) {
        selector.selectedKeys()
                .stream()
                .filter(SelectionKey::isValid)
                .filter(SelectionKey::isReadable)
                .forEach(key -> handleKey(key, selector));
    }

    @SneakyThrows
    private void handleKey(SelectionKey key, Selector selector) {
        receiver.receive(key, selector, receivedMessageTrigger());
    }

    private Consumer<String> receivedMessageTrigger() {
        return message -> {
            try {
                natsConnection.publish(config.subject(),
                        messageConverter.convert(message).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new SensorHandlerFailedException("Failed to process message", e);
            }
        };
    }
}

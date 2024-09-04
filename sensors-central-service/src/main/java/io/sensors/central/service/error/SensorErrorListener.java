package io.sensors.central.service.error;

import io.nats.client.Connection;
import io.nats.client.ErrorListener;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@Slf4j
public class SensorErrorListener implements ErrorListener {
    @Override
    public void errorOccurred(Connection connection, String error) {
        log.error("Error detected with message: {}, Server INfo: {}", error, connection.getServerInfo());
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception e) {
        log.error("Exception thrown with cause: {}, Server INfo: {}", e.getMessage(), connection.getServerInfo());
    }

    @Override
    public void messageDiscarded(Connection connection, Message msg) {
        log.error("Message '{}' discarded for Subject: {}, Server Info: {}",
                new String(msg.getData(), Charset.defaultCharset()), msg.getSubject(), connection.getServerInfo());
    }
}

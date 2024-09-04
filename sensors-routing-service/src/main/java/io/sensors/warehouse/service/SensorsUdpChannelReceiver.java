package io.sensors.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.function.Consumer;

@Component
@Slf4j
public class SensorsUdpChannelReceiver {
    private static final int BUFFER_SIZE = 256;

    public Selector createChannel(final int port) throws IOException {
        try (var selector = Selector.open(); var channel = DatagramChannel.open()) {

            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);

            return selector;
        }
    }

    public void receive(final SelectionKey key, final Selector selector, Consumer<String> trigger) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress clientAddress = (InetSocketAddress) datagramChannel.receive(buffer);
        log.info("Received sensor from {}", clientAddress.getAddress().getHostAddress());
        buffer.flip();
        String message = new String(buffer.array(), 0, buffer.limit());
        trigger.accept(message);
        selector.selectedKeys().remove(key);
    }
}

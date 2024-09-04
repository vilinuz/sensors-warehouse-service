package io.sensors.warehouse.testcontainer;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NatsContainer extends GenericContainer<NatsContainer> {
    private final static Path NATS_DOCKERFILE = Paths.get("..")
            .toAbsolutePath()
            .resolve(Paths.get("nats", "Dockerfile"));

    private static final int NATS_PORT = 4222;

    public NatsContainer() {
        super(new ImageFromDockerfile("sensor-nats-server").withDockerfile(NATS_DOCKERFILE));
        withExposedPorts(NATS_PORT);
    }

    public String getServerUrl() {
        return String.format("nats://%s:%d", getHost(), getMappedPort(NATS_PORT));
    }

    public String getUser() {
        return "sensor-publisher";
    }

    public String getPassword() {
        return "changeme";
    }
}
package io.sensors.warehouse.service;

import io.sensors.warehouse.exception.SensorHandlerFailedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * This is the Entry Point of sensors-routing-service
 * I recognise that for such a simple case using virtual threads is a big overkill,
 * but my strategy for the task is to play around with as diverse cutting-age tech as possible,
 * to make the services 100% non-blocking, (if not production ready, at least close enough),
 * let's say, after I spend some time to encrypt the NATS password and move it to a VAULT or ConfigMap,
 * configure actuator, make it cloud ready (at least for Kubernetes) and so on..
 * Instead, I put my focus on fully automate the deployment to Docker using Bash and docker-compose,
 * make sure that the end-to-end flow just works and is stable and reliable and prepare some unit tests.
 */
@Slf4j
@Component
public class WarehouseSensorsService {
    private final List<SensorsMessageHandler> handlers;

    public WarehouseSensorsService(final List<SensorsMessageHandler> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    public void startServices() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            handlers.forEach(handler -> executor.submit(startService(handler)));
        }
    }

    private static Runnable startService(final SensorsMessageHandler handler) {
        return () -> {
            try {
                handler.start();
            } catch (IOException e) {
                throw new SensorHandlerFailedException(
                        String.format("The Sensor Message Handler for subject: %s failed to start with error message: %s",
                                handler.getConfig().subject(), e.getMessage()));
            }
        };
    }
}
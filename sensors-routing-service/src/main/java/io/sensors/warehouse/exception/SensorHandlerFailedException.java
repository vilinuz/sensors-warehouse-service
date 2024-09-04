package io.sensors.warehouse.exception;


public class SensorHandlerFailedException extends RuntimeException {
    public SensorHandlerFailedException(String message) {
        super(message);
    }

    public SensorHandlerFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

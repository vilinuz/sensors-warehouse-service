package io.sensors.warehouse.exception;

public class SensorMessageValidationException extends RuntimeException {
    public SensorMessageValidationException(String message) {
        super(message);
    }

    public SensorMessageValidationException(String message, Object... args) {
        super(message);
    }
}

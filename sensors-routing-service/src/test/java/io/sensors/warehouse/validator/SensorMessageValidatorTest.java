package io.sensors.warehouse.validator;

import io.sensors.warehouse.exception.SensorMessageValidationException;
import io.sensors.warehouse.fixtures.TestData;
import io.sensors.warehouse.model.SensorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SensorMessageValidatorTest {

    private SensorMessageValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SensorMessageValidator(TestData.createWarehousePropertiesConfig());
    }

    @Test
    void validate() {
        assertThrows(SensorMessageValidationException.class,
                () -> validator.validate(new SensorMessage("invalid", "100")));
    }
}
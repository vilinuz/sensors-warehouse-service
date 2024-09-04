package io.sensors.warehouse.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sensors.warehouse.exception.SensorMessageValidationException;
import io.sensors.warehouse.fixtures.TestData;
import io.sensors.warehouse.validator.SensorMessageValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SensorsMessageConverterTest {

    private SensorsMessageConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SensorsMessageConverter(
                new SensorMessageValidator(TestData.createWarehousePropertiesConfig()), new ObjectMapper());
    }

    @Test
    void convert_withValidMessage() throws JsonProcessingException {
        String message = converter.convert("sensor_id=t1; value=30");

        assertThat(message).isEqualTo("{\"id\":\"t1\",\"value\":\"30\"}");
    }

    @Test
    void convert_withInvalidMessage() {
        assertThrows(SensorMessageValidationException.class,
                () -> converter.convert("sensor_id=t1; invalid value=30"));
    }
}
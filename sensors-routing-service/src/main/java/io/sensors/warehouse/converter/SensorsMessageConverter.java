package io.sensors.warehouse.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sensors.warehouse.exception.SensorMessageValidationException;
import io.sensors.warehouse.model.SensorMessage;
import io.sensors.warehouse.validator.SensorMessageValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SensorsMessageConverter {
    private static final String REGEX_PATTERN = "^sensor_id=([a-zA-Z0-9]{2});\\s*value=(\\d{2})$";
    private static final Pattern MESSAGE_PATTERN = Pattern.compile(REGEX_PATTERN);

    private final SensorMessageValidator validator;
    private final ObjectMapper objectMapper;

    public SensorsMessageConverter(SensorMessageValidator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    public String convert(String message) throws JsonProcessingException {
        String normalizedMessage = normalizeMessage(message);
        log.info("Validating format for message: '{}'", normalizedMessage);
        Matcher matcher = MESSAGE_PATTERN.matcher(normalizedMessage);

        if (matcher.matches()) {
            SensorMessage sensorMessage = new SensorMessage(matcher.group(1), matcher.group(2));
            validator.validate(sensorMessage);
            log.info("Message '{}' successfully validated", normalizedMessage);
            return objectMapper.writeValueAsString(sensorMessage);
        }

        throw new SensorMessageValidationException(String.format("Message %s has invalid format", message));
    }

    private String normalizeMessage(String message) {
        return message
                .replaceAll("[\n\r]", "")
                .replaceAll(" ", "");
    }
}

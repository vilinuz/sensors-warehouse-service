package io.sensors.central.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.sensors.central.service.config.props.WarehousePropertiesConfig;
import io.sensors.central.service.error.SensorErrorListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties({
        WarehousePropertiesConfig.class
})
public class WarehouseConfig {

    @Bean
    @ConditionalOnMissingBean
    public Connection natsConnection(final WarehousePropertiesConfig config,
                                     final SensorErrorListener errorListener) throws IOException, InterruptedException {
        Options options = new Options.Builder()
                .server(config.nats().server().uri())
                .userInfo(config.nats().server().user(), config.nats().server().password())
                .errorListener(errorListener)
                .build();
        return Nats.connect(options);
    }

    @Bean
    @ConditionalOnMissingBean
    public SensorErrorListener errorListener() {
        return new SensorErrorListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();

        return objectMapper
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }
}

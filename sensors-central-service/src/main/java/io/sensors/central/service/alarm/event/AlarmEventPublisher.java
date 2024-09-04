package io.sensors.central.service.alarm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(final AlarmEvent event) {
        publisher.publishEvent(event);
        log.info("Published alarm event {}", event);
    }
}

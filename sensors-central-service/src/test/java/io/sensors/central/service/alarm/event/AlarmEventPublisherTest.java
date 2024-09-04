package io.sensors.central.service.alarm.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlarmEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private AlarmEventPublisher eventPublisher;

    @Test
    void publish() {
        eventPublisher.publish(AlarmEvent.builder().build());

        verify(applicationEventPublisher, times(1)).publishEvent(any(Object.class));
    }
}

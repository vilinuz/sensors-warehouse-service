package io.sensors.central.service.alarm.event;

import lombok.Builder;

@Builder
public record AlarmEvent(String sensorId, int value, int threshold) {
}

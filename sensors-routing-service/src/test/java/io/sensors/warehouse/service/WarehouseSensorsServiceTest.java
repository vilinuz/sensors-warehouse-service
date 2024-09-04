package io.sensors.warehouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WarehouseSensorsServiceTest {

    @Mock
    private SensorsMessageHandler handler1;

    @Mock
    private SensorsMessageHandler handler2;

    @InjectMocks
    private WarehouseSensorsService warehouseSensorsService;

    @BeforeEach
    public void setUp() {
        warehouseSensorsService = new WarehouseSensorsService(List.of(handler1, handler2));
    }

    @Test
    public void testStartServices() throws IOException {
        warehouseSensorsService.startServices();

        verify(handler1, times(1)).start();
        verify(handler2, times(1)).start();
    }
}
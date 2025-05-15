package com.foodquart.microservicetraceability.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class OrderEfficiencyModelTest {

    @Test
    void testToString() {
        OrderEfficiencyModel efficiency = OrderEfficiencyModel.builder()
                .orderId(123L)
                .processingTime(Duration.ofMinutes(10).plusSeconds(30))
                .build();
        String toStringResult = efficiency.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("123"));
        assertTrue(toStringResult.contains("PT10M30S"));
    }

    @Test
    void testGetFormattedProcessingTime() {
        OrderEfficiencyModel efficiency = OrderEfficiencyModel.builder()
                .orderId(123L)
                .processingTime(Duration.ofMinutes(10).plusSeconds(30))
                .build();
        assertEquals("10 minutes 30 seconds", efficiency.getFormattedProcessingTime());
    }

    @Test
    void testGetTotalSeconds() {
        OrderEfficiencyModel efficiency = OrderEfficiencyModel.builder()
                .orderId(123L)
                .processingTime(Duration.ofMinutes(10).plusSeconds(30))
                .build();
        assertEquals(630, efficiency.getTotalSeconds());

        OrderEfficiencyModel nullEfficiency = OrderEfficiencyModel.builder()
                .orderId(456L)
                .processingTime(null)
                .build();
        assertEquals(0, nullEfficiency.getTotalSeconds());
    }

    @Test
    void testGetTotalMinutes() {
        OrderEfficiencyModel efficiency = OrderEfficiencyModel.builder()
                .orderId(123L)
                .processingTime(Duration.ofMinutes(10).plusSeconds(30))
                .build();
        assertEquals(10.5, efficiency.getTotalMinutes(), 0.001);

        OrderEfficiencyModel nullEfficiency = OrderEfficiencyModel.builder()
                .orderId(456L)
                .processingTime(null)
                .build();
        assertEquals(0.0, nullEfficiency.getTotalMinutes(), 0.001);
    }

    @Test
    void testOrderIdCoverage() {
        OrderEfficiencyModel efficiency = OrderEfficiencyModel.builder()
                .orderId(123L)
                .processingTime(Duration.ZERO)
                .build();
        assertNotNull(efficiency.getOrderId());
    }

}
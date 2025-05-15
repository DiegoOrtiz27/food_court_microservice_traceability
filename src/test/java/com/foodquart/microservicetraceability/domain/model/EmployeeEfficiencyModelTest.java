package com.foodquart.microservicetraceability.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeEfficiencyModelTest {

    @Test
    void testGetFormattedAverageTime() {
        EmployeeEfficiencyModel efficiency = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofMinutes(5).plusSeconds(30))
                .completedOrdersCount(10L)
                .build();
        assertEquals("5 minutes 30 seconds", efficiency.getFormattedAverageTime());

        EmployeeEfficiencyModel efficiencyZeroSeconds = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofMinutes(5))
                .completedOrdersCount(10L)
                .build();
        assertEquals("5 minutes 0 seconds", efficiencyZeroSeconds.getFormattedAverageTime());

        EmployeeEfficiencyModel efficiencyZeroMinutes = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofSeconds(30))
                .completedOrdersCount(10L)
                .build();
        assertEquals("0 minutes 30 seconds", efficiencyZeroMinutes.getFormattedAverageTime());
    }

    @Test
    void testGetAverageInMinutes() {
        EmployeeEfficiencyModel efficiency = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofMinutes(5).plusSeconds(30))
                .completedOrdersCount(10L)
                .build();
        assertEquals(5.5, efficiency.getAverageInMinutes(), 0.001);

        EmployeeEfficiencyModel efficiencyFractional = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofSeconds(90)) // 1 minuto 30 segundos
                .completedOrdersCount(10L)
                .build();
        assertEquals(1.5, efficiencyFractional.getAverageInMinutes(), 0.001);

        EmployeeEfficiencyModel efficiencyZeroSeconds = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ofMinutes(5))
                .completedOrdersCount(10L)
                .build();
        assertEquals(5.0, efficiencyZeroSeconds.getAverageInMinutes(), 0.001);
    }

    @Test
    void testGetCompletedOrdersCountAsInt() {
        EmployeeEfficiencyModel efficiency = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ZERO)
                .completedOrdersCount(10L)
                .build();
        assertEquals(10, efficiency.getCompletedOrdersCountAsInt());

        EmployeeEfficiencyModel efficiencyZero = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ZERO)
                .completedOrdersCount(0L)
                .build();
        assertEquals(0, efficiencyZero.getCompletedOrdersCountAsInt());

        EmployeeEfficiencyModel efficiencyNull = EmployeeEfficiencyModel.builder()
                .employeeId(123L)
                .averageProcessingTime(Duration.ZERO)
                .completedOrdersCount(null)
                .build();
        assertEquals(0, efficiencyNull.getCompletedOrdersCountAsInt());
    }

    @Test
    void testGetters() {
        Long employeeId = 456L;
        Duration averageTime = Duration.ofMinutes(7);
        Long completedCount = 5L;
        EmployeeEfficiencyModel efficiency = EmployeeEfficiencyModel.builder()
                .employeeId(employeeId)
                .averageProcessingTime(averageTime)
                .completedOrdersCount(completedCount)
                .build();
        assertEquals(employeeId, efficiency.getEmployeeId());
        assertEquals(averageTime, efficiency.getAverageProcessingTime());
        assertEquals(completedCount, efficiency.getCompletedOrdersCount());
    }
}
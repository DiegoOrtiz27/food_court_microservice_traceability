package com.foodquart.microservicetraceability.domain.model;

import lombok.*;

import java.time.Duration;

@Getter
@Builder
public class EmployeeEfficiencyModel {
    private final Long employeeId;
    private final Duration averageProcessingTime;
    private final Long completedOrdersCount;

    public String getFormattedAverageTime() {
        long minutes = averageProcessingTime.toMinutes();
        long seconds = averageProcessingTime.minusMinutes(minutes).getSeconds();
        return minutes + " minutes " + seconds + " seconds";
    }

    public double getAverageInMinutes() {
        return averageProcessingTime.toMinutes() +
                (averageProcessingTime.getSeconds() % 60) / 60.0;
    }

    public int getCompletedOrdersCountAsInt() {
        return completedOrdersCount != null ? completedOrdersCount.intValue() : 0;
    }
}

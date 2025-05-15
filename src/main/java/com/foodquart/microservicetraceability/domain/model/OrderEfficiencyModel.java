package com.foodquart.microservicetraceability.domain.model;

import lombok.*;

import java.time.Duration;

@Getter
@Builder
@ToString
public class OrderEfficiencyModel {
    private final Long orderId;
    private final Duration processingTime;

    public String getFormattedProcessingTime() {
        long minutes = processingTime.toMinutes();
        long seconds = processingTime.minusMinutes(minutes).getSeconds();
        return minutes + " minutes " + seconds + " seconds";
    }

    public long getTotalSeconds() {
        return processingTime != null ? processingTime.getSeconds() : 0;
    }

    public double getTotalMinutes() {
        return processingTime != null ? processingTime.toMillis() / 60000.0 : 0;
    }

}
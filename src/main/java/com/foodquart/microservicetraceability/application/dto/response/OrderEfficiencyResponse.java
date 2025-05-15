package com.foodquart.microservicetraceability.application.dto.response;

import lombok.*;

@Getter
@Setter
public class OrderEfficiencyResponse {
    private Long orderId;
    private String processingTime;
    private long totalSeconds;
    private double totalMinutes;
}
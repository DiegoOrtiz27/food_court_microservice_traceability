package com.foodquart.microservicetraceability.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderTraceResponse {
    private String id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private String previousStatus;
    private String newStatus;
    private LocalDateTime timestamp;
    private String notes;
}

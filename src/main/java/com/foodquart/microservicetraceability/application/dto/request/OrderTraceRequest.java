package com.foodquart.microservicetraceability.application.dto.request;

import com.foodquart.microservicetraceability.domain.util.OrderStatus;
import lombok.*;

@Getter
@Setter
public class OrderTraceRequest {
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String notes;
}
package com.foodquart.microservicetraceability.infrastructure.out.jpa.entity;

import com.foodquart.microservicetraceability.domain.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_traces")
public class OrderTraceEntity {
    @Id
    private String id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long employeeId;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private LocalDateTime timestamp;
    private String notes;
}

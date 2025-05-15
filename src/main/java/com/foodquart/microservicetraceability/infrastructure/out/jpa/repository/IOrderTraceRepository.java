package com.foodquart.microservicetraceability.infrastructure.out.jpa.repository;

import com.foodquart.microservicetraceability.infrastructure.out.jpa.entity.OrderTraceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IOrderTraceRepository extends MongoRepository<OrderTraceEntity, String> {
    List<OrderTraceEntity> findByCustomerIdAndOrderId(Long customerId, Long orderId);

    List<OrderTraceEntity> findByRestaurantId(Long restaurantId);
}
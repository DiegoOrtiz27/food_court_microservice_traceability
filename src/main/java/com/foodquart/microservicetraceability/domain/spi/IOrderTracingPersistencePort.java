package com.foodquart.microservicetraceability.domain.spi;

import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;

import java.util.List;

public interface IOrderTracingPersistencePort {

    OrderTraceModel saveTrace(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> findByCustomerIdAndOrderId(Long customerId, Long orderId);

    List<OrderTraceModel> findByRestaurantId(Long restaurantId);
}
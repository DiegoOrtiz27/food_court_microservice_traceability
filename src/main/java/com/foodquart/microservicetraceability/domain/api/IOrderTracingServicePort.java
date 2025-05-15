package com.foodquart.microservicetraceability.domain.api;

import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;

import java.util.List;

public interface IOrderTracingServicePort {

    OrderTraceModel recordOrderStatusChange(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> getOrderTracesByCustomer(Long orderId);
}

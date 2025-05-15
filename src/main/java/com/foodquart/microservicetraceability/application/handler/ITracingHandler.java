package com.foodquart.microservicetraceability.application.handler;

import com.foodquart.microservicetraceability.application.dto.request.OrderTraceRequest;
import com.foodquart.microservicetraceability.application.dto.response.OrderTraceResponse;

import java.util.List;

public interface ITracingHandler {
    List<OrderTraceResponse> getOrderTraces(Long orderId);

    OrderTraceResponse recordOrderStatusChange(OrderTraceRequest orderTraceRequest);
}

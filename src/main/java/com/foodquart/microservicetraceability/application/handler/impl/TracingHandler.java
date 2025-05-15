package com.foodquart.microservicetraceability.application.handler.impl;

import com.foodquart.microservicetraceability.application.dto.request.OrderTraceRequest;
import com.foodquart.microservicetraceability.application.dto.response.OrderTraceResponse;
import com.foodquart.microservicetraceability.application.handler.ITracingHandler;
import com.foodquart.microservicetraceability.application.mapper.request.ITracingRequestMapper;
import com.foodquart.microservicetraceability.application.mapper.response.ITracingResponseMapper;
import com.foodquart.microservicetraceability.domain.api.IOrderTracingServicePort;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TracingHandler implements ITracingHandler {

    private final IOrderTracingServicePort tracingServicePort;
    private final ITracingRequestMapper tracingRequestMapper;
    private final ITracingResponseMapper tracingResponseMapper;

    public OrderTraceResponse recordOrderStatusChange(OrderTraceRequest orderTraceRequest) {
        OrderTraceModel orderTrace = tracingRequestMapper.toModel(orderTraceRequest);
        OrderTraceModel savedTrace = tracingServicePort.recordOrderStatusChange(orderTrace);
        return tracingResponseMapper.toResponse(savedTrace);
    }

    @Override
    public List<OrderTraceResponse> getOrderTraces(Long orderId) {
        return tracingServicePort.getOrderTracesByCustomer(orderId)
                .stream()
                .map(tracingResponseMapper::toResponse)
                .toList();
    }
}

package com.foodquart.microservicetraceability.application.handler.impl;

import com.foodquart.microservicetraceability.application.dto.request.OrderTraceRequest;
import com.foodquart.microservicetraceability.application.dto.response.EmployeeEfficiencyResponse;
import com.foodquart.microservicetraceability.application.dto.response.OrderEfficiencyResponse;
import com.foodquart.microservicetraceability.application.dto.response.OrderTraceResponse;
import com.foodquart.microservicetraceability.application.handler.ITracingHandler;
import com.foodquart.microservicetraceability.application.mapper.request.ITracingRequestMapper;
import com.foodquart.microservicetraceability.application.mapper.response.IEmployeeEfficiencyResponseMapper;
import com.foodquart.microservicetraceability.application.mapper.response.IOrderEfficiencyResponseMapper;
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
    private final IOrderEfficiencyResponseMapper orderEfficiencyResponseMapper;
    private final IEmployeeEfficiencyResponseMapper employeeEfficiencyResponseMapper;

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

    @Override
    public List<OrderEfficiencyResponse> getOrderEfficiencies(Long restaurantId) {
        return tracingServicePort.calculateOrderEfficiencies(restaurantId)
                .stream()
                .map(orderEfficiencyResponseMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeEfficiencyResponse> getEmployeeEfficiencyRanking(Long restaurantId) {
        return tracingServicePort.calculateEmployeeEfficiencies(restaurantId)
                .stream()
                .map(employeeEfficiencyResponseMapper::toResponse)
                .toList();
    }
}

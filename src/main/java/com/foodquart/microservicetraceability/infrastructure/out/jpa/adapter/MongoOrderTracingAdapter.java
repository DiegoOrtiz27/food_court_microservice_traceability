package com.foodquart.microservicetraceability.infrastructure.out.jpa.adapter;

import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import com.foodquart.microservicetraceability.domain.spi.IOrderTracingPersistencePort;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.entity.OrderTraceEntity;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.mapper.IOrderTracingEntityMapper;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MongoOrderTracingAdapter implements IOrderTracingPersistencePort {

    private final IOrderTraceRepository orderTraceRepository;
    private final IOrderTracingEntityMapper tracingEntityMapper;

    @Override
    public OrderTraceModel saveTrace(OrderTraceModel orderTraceModel) {
        OrderTraceEntity entity = tracingEntityMapper.toEntity(orderTraceModel);
        OrderTraceEntity savedEntity = orderTraceRepository.save(entity);
        return tracingEntityMapper.toOrderTraceModel(savedEntity);
    }

    @Override
    public List<OrderTraceModel> findByCustomerIdAndOrderId(Long customerId, Long orderId) {
        return orderTraceRepository.findByCustomerIdAndOrderId(customerId, orderId)
                .stream()
                .map(tracingEntityMapper::toOrderTraceModel)
                .collect(Collectors.toList());
    }
}
package com.foodquart.microservicetraceability.infrastructure.out.jpa.mapper;

import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.entity.OrderTraceEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderTracingEntityMapper {

    OrderTraceEntity toEntity(OrderTraceModel orderTraceModel);

    OrderTraceModel toOrderTraceModel(OrderTraceEntity entity);
}

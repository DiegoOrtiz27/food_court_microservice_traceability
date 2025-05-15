package com.foodquart.microservicetraceability.application.mapper.response;

import com.foodquart.microservicetraceability.application.dto.response.OrderTraceResponse;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITracingResponseMapper {

    OrderTraceResponse toResponse(OrderTraceModel orderTraceModel);
}

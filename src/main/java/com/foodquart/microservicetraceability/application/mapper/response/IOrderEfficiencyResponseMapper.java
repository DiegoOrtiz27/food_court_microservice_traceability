package com.foodquart.microservicetraceability.application.mapper.response;

import com.foodquart.microservicetraceability.application.dto.response.OrderEfficiencyResponse;
import com.foodquart.microservicetraceability.domain.model.OrderEfficiencyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEfficiencyResponseMapper {

    @Mapping(target = "processingTime", source = "formattedProcessingTime")
    OrderEfficiencyResponse toResponse(OrderEfficiencyModel efficiency);
}

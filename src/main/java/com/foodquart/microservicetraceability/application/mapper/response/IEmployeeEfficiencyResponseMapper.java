package com.foodquart.microservicetraceability.application.mapper.response;

import com.foodquart.microservicetraceability.application.dto.response.EmployeeEfficiencyResponse;
import com.foodquart.microservicetraceability.domain.model.EmployeeEfficiencyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeEfficiencyResponseMapper {

    @Mapping(target = "averageProcessingTime", source = "formattedAverageTime")
    @Mapping(target = "averageMinutes", source = "averageInMinutes")
    @Mapping(target = "completedOrdersCount", source = "completedOrdersCountAsInt")
    EmployeeEfficiencyResponse toResponse(EmployeeEfficiencyModel efficiency);
}

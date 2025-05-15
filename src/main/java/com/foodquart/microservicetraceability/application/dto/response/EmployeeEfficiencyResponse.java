package com.foodquart.microservicetraceability.application.dto.response;

import lombok.*;

@Getter
@Setter
public class EmployeeEfficiencyResponse {
    private Long employeeId;
    private String averageProcessingTime;
    private double averageMinutes;
    private int completedOrdersCount;
}

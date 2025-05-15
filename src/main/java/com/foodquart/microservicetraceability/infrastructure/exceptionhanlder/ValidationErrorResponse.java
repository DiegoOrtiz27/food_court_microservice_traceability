package com.foodquart.microservicetraceability.infrastructure.exceptionhanlder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private String errorCode;
    private String message;
    private List<ValidationError> errors;
}

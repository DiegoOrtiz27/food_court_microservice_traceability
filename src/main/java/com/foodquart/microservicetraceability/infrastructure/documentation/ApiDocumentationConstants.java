package com.foodquart.microservicetraceability.infrastructure.documentation;

public class ApiDocumentationConstants {
    
    public static final String RECORD_TRACE_SUMMARY = "Creates a new trace record for an order status transition";
    public static final String RECORD_TRACE_DESC = "Order trace details";
    public static final String RECORD_TRACE_SUCCESS = "Trace record created successfully";

    public static final String GET_TRACES_SUMMARY = "Retrieves all status change traces for a specific order";
    public static final String GET_TRACES_SUCCESS = "List of order traces retrieved successfully";

    public static final String ORDER_EFFICIENCIES_SUMMARY = "Get order processing efficiencies for a restaurant";
    public static final String ORDER_EFFICIENCIES_SUCCESS = "List of order efficiencies retrieved successfully";

    public static final String EMPLOYEE_EFFICIENCY_SUMMARY = "Get employee efficiency ranking for a restaurant";
    public static final String EMPLOYEE_EFFICIENCY_SUCCESS = "Employee ranking retrieved successfully";

    public static final String RESPONSE_400 = "Invalid input data";
    public static final String RESPONSE_401 = "Unauthorized - Authentication required";
    public static final String RESPONSE_403 = "Forbidden - Insufficient permissions";
    public static final String RESPONSE_404 = "Resource not found";

    private ApiDocumentationConstants() {
    }
}

package com.foodquart.microservicetraceability.domain.util;

public class TracingMessages {
    public static final String ORDER_ID_REQUIRED = "Order ID is required";
    public static final String STATUS_REQUIRED = "Order status is required";
    public static final String CUSTOMER_ID_REQUIRED = "Customer ID is required";

    private TracingMessages() {
        throw new IllegalStateException("Utility class");
    }
}

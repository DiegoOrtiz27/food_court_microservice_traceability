package com.foodquart.microservicetraceability.domain.util;

public final class HttpMessages {
    public static final String VALIDATION_FAILED = "Validation failed for one or more fields";
    public static final String UNKNOWN_FIELD = "Unknown field '%s' in request. Accepted fields: %s";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    private HttpMessages() {
        throw new IllegalStateException("Utility class");
    }
}

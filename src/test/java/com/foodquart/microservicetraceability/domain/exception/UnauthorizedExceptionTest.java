package com.foodquart.microservicetraceability.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void shouldCreateUnauthorizedExceptionWithMessage() {
        String errorMessage = "Authentication failed.";
        UnauthorizedException exception = new UnauthorizedException(errorMessage);
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

}
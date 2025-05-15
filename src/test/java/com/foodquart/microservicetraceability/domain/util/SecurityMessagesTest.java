package com.foodquart.microservicetraceability.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityMessagesTest {

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, SecurityMessages::new, "Utility class");
    }

    @Test
    void publicPathsShouldContainExpectedValues() {
        assertTrue(SecurityMessages.PUBLIC_PATHS.contains("/swagger-ui.html"));
        assertTrue(SecurityMessages.PUBLIC_PATHS.contains("/v3/api-docs"));
        assertEquals(6, SecurityMessages.PUBLIC_PATHS.size());
    }
}
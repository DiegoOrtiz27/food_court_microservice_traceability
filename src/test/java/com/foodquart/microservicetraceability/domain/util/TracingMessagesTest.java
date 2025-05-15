package com.foodquart.microservicetraceability.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TracingMessagesTest {

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, TracingMessages::new, "Utility class");
    }
}
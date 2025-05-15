package com.foodquart.microservicetraceability.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void constructorShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, ValidationUtil::new, "Utility class");
    }
}
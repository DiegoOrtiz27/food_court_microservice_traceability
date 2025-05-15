package com.foodquart.microservicetraceability.domain.model;

import com.foodquart.microservicetraceability.domain.util.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTraceModelTest {

    @Test
    void testSettersAndGetters() {
        OrderTraceModel trace = new OrderTraceModel();
        String id = "testId";
        Long orderId = 123L;
        Long customerId = 456L;
        Long restaurantId = 789L;
        Long employeeId = 101L;
        OrderStatus previousStatus = OrderStatus.PENDING;
        OrderStatus newStatus = OrderStatus.IN_PREPARATION;
        LocalDateTime timestamp = LocalDateTime.now();
        String notes = "Test notes";

        trace.setId(id);
        trace.setOrderId(orderId);
        trace.setCustomerId(customerId);
        trace.setRestaurantId(restaurantId);
        trace.setEmployeeId(employeeId);
        trace.setPreviousStatus(previousStatus);
        trace.setNewStatus(newStatus);
        trace.setTimestamp(timestamp);
        trace.setNotes(notes);

        assertEquals(id, trace.getId());
        assertEquals(orderId, trace.getOrderId());
        assertEquals(customerId, trace.getCustomerId());
        assertEquals(restaurantId, trace.getRestaurantId());
        assertEquals(employeeId, trace.getEmployeeId());
        assertEquals(previousStatus, trace.getPreviousStatus());
        assertEquals(newStatus, trace.getNewStatus());
        assertEquals(timestamp, trace.getTimestamp());
        assertEquals(notes, trace.getNotes());
    }

    @Test
    void testNoArgsConstructor() {
        OrderTraceModel trace = new OrderTraceModel();
        assertNull(trace.getId());
        assertNull(trace.getOrderId());
        assertNull(trace.getCustomerId());
        assertNull(trace.getRestaurantId());
        assertNull(trace.getEmployeeId());
        assertNull(trace.getPreviousStatus());
        assertNull(trace.getNewStatus());
        assertNull(trace.getTimestamp());
        assertNull(trace.getNotes());
    }

    @Test
    void testAllArgsConstructor() {
        String id = "testId";
        Long orderId = 123L;
        Long customerId = 456L;
        Long restaurantId = 789L;
        Long employeeId = 101L;
        OrderStatus previousStatus = OrderStatus.PENDING;
        OrderStatus newStatus = OrderStatus.IN_PREPARATION;
        LocalDateTime timestamp = LocalDateTime.now();
        String notes = "Test notes";

        OrderTraceModel trace = new OrderTraceModel(id, orderId, customerId, restaurantId, employeeId, previousStatus, newStatus, timestamp, notes);

        assertEquals(id, trace.getId());
        assertEquals(orderId, trace.getOrderId());
        assertEquals(customerId, trace.getCustomerId());
        assertEquals(restaurantId, trace.getRestaurantId());
        assertEquals(employeeId, trace.getEmployeeId());
        assertEquals(previousStatus, trace.getPreviousStatus());
        assertEquals(newStatus, trace.getNewStatus());
        assertEquals(timestamp, trace.getTimestamp());
        assertEquals(notes, trace.getNotes());
    }
}
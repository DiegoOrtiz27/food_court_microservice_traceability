package com.foodquart.microservicetraceability.domain.usecase;

import com.foodquart.microservicetraceability.domain.exception.DomainException;
import com.foodquart.microservicetraceability.domain.model.EmployeeEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import com.foodquart.microservicetraceability.domain.spi.IOrderTracingPersistencePort;
import com.foodquart.microservicetraceability.domain.spi.ISecurityContextPort;
import com.foodquart.microservicetraceability.domain.util.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.foodquart.microservicetraceability.domain.util.TracingMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderTracingUseCase Tests")
class OrderTracingUseCaseTest {

    @Mock
    private IOrderTracingPersistencePort tracingPersistencePort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private OrderTracingUseCase orderTracingUseCase;

    private OrderTraceModel testTrace;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        testTrace = new OrderTraceModel(null, 1L, 101L, 201L, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null);
    }

    @Nested
    @DisplayName("recordOrderStatusChange Tests")
    class RecordOrderStatusChangeTests {

        @Test
        @DisplayName("Should save trace with timestamp when valid data is provided")
        void shouldSaveTraceWithTimestamp() {
            OrderTraceModel inputTrace = new OrderTraceModel(null, 1L, 101L, 201L, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, null, null);
            OrderTraceModel savedTrace = new OrderTraceModel(null, 1L, 101L, 201L, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null);

            when(tracingPersistencePort.saveTrace(any(OrderTraceModel.class))).thenReturn(savedTrace);

            OrderTraceModel result = orderTracingUseCase.recordOrderStatusChange(inputTrace);

            assertNotNull(result.getTimestamp());
            assertEquals(inputTrace.getOrderId(), result.getOrderId());
            assertEquals(inputTrace.getCustomerId(), result.getCustomerId());
            assertEquals(inputTrace.getNewStatus(), result.getNewStatus());
            verify(tracingPersistencePort).saveTrace(any(OrderTraceModel.class));
        }

        @Test
        @DisplayName("Should throw DomainException when orderId is null")
        void shouldThrowWhenOrderIdIsNull() {
            OrderTraceModel invalidTrace = new OrderTraceModel(null, null, 101L, 201L, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderTracingUseCase.recordOrderStatusChange(invalidTrace));

            assertEquals(ORDER_ID_REQUIRED, exception.getMessage());
            verify(tracingPersistencePort, never()).saveTrace(any());
        }

        @Test
        @DisplayName("Should throw DomainException when customerId is null")
        void shouldThrowWhenCustomerIdIsNull() {
            OrderTraceModel invalidTrace = new OrderTraceModel(null, 1L, null, 201L, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderTracingUseCase.recordOrderStatusChange(invalidTrace));

            assertEquals(CUSTOMER_ID_REQUIRED, exception.getMessage());
            verify(tracingPersistencePort, never()).saveTrace(any());
        }

        @Test
        @DisplayName("Should throw DomainException when newStatus is null")
        void shouldThrowWhenNewStatusIsNull() {
            OrderTraceModel invalidTrace = new OrderTraceModel(null, 1L, 101L, 201L, null, OrderStatus.PENDING, null, now, null);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderTracingUseCase.recordOrderStatusChange(invalidTrace));

            assertEquals(STATUS_REQUIRED, exception.getMessage());
            verify(tracingPersistencePort, never()).saveTrace(any());
        }
    }

    @Nested
    @DisplayName("getOrderTracesByCustomer Tests")
    class GetOrderTracesByCustomerTests {

        @Test
        @DisplayName("Should return list of traces for given order and customer")
        void shouldReturnTracesForOrderAndCustomer() {
            Long orderId = 1L;
            Long customerId = 101L;
            List<OrderTraceModel> traces = Collections.singletonList(testTrace);

            when(securityContextPort.getCurrentUserId()).thenReturn(customerId);
            when(tracingPersistencePort.findByCustomerIdAndOrderId(customerId, orderId)).thenReturn(traces);

            List<OrderTraceModel> result = orderTracingUseCase.getOrderTracesByCustomer(orderId);

            assertEquals(traces, result);
            verify(securityContextPort).getCurrentUserId();
            verify(tracingPersistencePort).findByCustomerIdAndOrderId(customerId, orderId);
        }

        @Test
        @DisplayName("Should throw DomainException when current customerId is null")
        void shouldThrowWhenCurrentCustomerIdIsNull() {
            Long orderId = 1L;

            when(securityContextPort.getCurrentUserId()).thenReturn(null);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderTracingUseCase.getOrderTracesByCustomer(orderId));

            assertEquals(CUSTOMER_ID_REQUIRED, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(tracingPersistencePort, never()).findByCustomerIdAndOrderId(any(), any());
        }

        @Test
        @DisplayName("Should throw DomainException when orderId is null")
        void shouldThrowWhenOrderIdIsNullForGetTraces() {
            Long customerId = 101L;

            when(securityContextPort.getCurrentUserId()).thenReturn(customerId);

            DomainException exception = assertThrows(DomainException.class,
                    () -> orderTracingUseCase.getOrderTracesByCustomer(null));

            assertEquals(ORDER_ID_REQUIRED, exception.getMessage());
            verify(securityContextPort).getCurrentUserId();
            verify(tracingPersistencePort, never()).findByCustomerIdAndOrderId(any(), any());
        }
    }

    @Nested
    @DisplayName("calculateOrderEfficiencies Tests")
    class CalculateOrderEfficienciesTests {

        @Test
        @DisplayName("Should calculate processing time for orders from PENDING to DELIVERED")
        void shouldCalculateOrderProcessingTimePendingToDelivered() {
            Long restaurantId = 201L;
            LocalDateTime startTime = now.minusMinutes(15);
            LocalDateTime readyTime = startTime.plusMinutes(5);
            LocalDateTime deliverTime = now;
            List<OrderTraceModel> traces = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, null, OrderStatus.PENDING, startTime, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, startTime.plusMinutes(1), null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.IN_PREPARATION, OrderStatus.READY, readyTime, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.READY, OrderStatus.DELIVERED, deliverTime, null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(traces);

            List<OrderEfficiencyModel> efficiencies = orderTracingUseCase.calculateOrderEfficiencies(restaurantId);

            assertEquals(1, efficiencies.size());
            assertEquals(Duration.between(startTime, deliverTime), efficiencies.getFirst().getProcessingTime());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }

        @Test
        @DisplayName("Should return empty list if no orders are found for the restaurant")
        void shouldReturnEmptyListIfNoOrdersFound() {
            Long restaurantId = 201L;

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

            List<OrderEfficiencyModel> efficiencies = orderTracingUseCase.calculateOrderEfficiencies(restaurantId);

            assertTrue(efficiencies.isEmpty());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }

        @Test
        @DisplayName("Should ignore orders without PENDING and DELIVERED status")
        void shouldIgnoreOrdersWithoutStartAndEndTimes() {
            Long restaurantId = 201L;
            List<OrderTraceModel> incompleteTraces = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.IN_PREPARATION, OrderStatus.READY, now.plusMinutes(5), null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(incompleteTraces);

            List<OrderEfficiencyModel> efficiencies = orderTracingUseCase.calculateOrderEfficiencies(restaurantId);

            assertTrue(efficiencies.isEmpty());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }

        @Test
        @DisplayName("Should handle orders that are cancelled")
        void shouldHandleCancelledOrders() {
            Long restaurantId = 201L;
            LocalDateTime startTime = now.minusMinutes(10);
            List<OrderTraceModel> traces = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, startTime, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, null, OrderStatus.IN_PREPARATION, OrderStatus.CANCELLED, now, null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(traces);

            List<OrderEfficiencyModel> efficiencies = orderTracingUseCase.calculateOrderEfficiencies(restaurantId);

            assertTrue(efficiencies.isEmpty());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }
    }

    @Nested
    @DisplayName("calculateEmployeeEfficiencies Tests")
    class CalculateEmployeeEfficienciesTests {

        @Test
        @DisplayName("Should calculate average processing time for employees from IN_PREPARATION to DELIVERED")
        void shouldCalculateEmployeeEfficiency() {
            Long restaurantId = 201L;
            Long employeeId1 = 301L;
            Long employeeId2 = 302L;
            LocalDateTime prepStart1 = now.minusMinutes(10);
            LocalDateTime deliveredTime1 = now;
            LocalDateTime prepStart2 = now.minusMinutes(5);
            LocalDateTime deliveredTime2 = now.plusMinutes(3);
            LocalDateTime prepStart3 = now.minusMinutes(12);
            LocalDateTime deliveredTime3 = now.minusMinutes(2);
            LocalDateTime prepStart4 = now.minusMinutes(8);
            LocalDateTime deliveredTime4 = now.plusMinutes(1);

            List<OrderTraceModel> traces = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, employeeId1, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, prepStart1, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, employeeId1, OrderStatus.IN_PREPARATION, OrderStatus.DELIVERED, deliveredTime1, null),
                    new OrderTraceModel(null, 2L, 102L, restaurantId, employeeId1, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, prepStart2, null),
                    new OrderTraceModel(null, 2L, 102L, restaurantId, employeeId1, OrderStatus.IN_PREPARATION, OrderStatus.DELIVERED, deliveredTime2, null),
                    new OrderTraceModel(null, 3L, 103L, restaurantId, employeeId2, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, prepStart3, null),
                    new OrderTraceModel(null, 3L, 103L, restaurantId, employeeId2, OrderStatus.IN_PREPARATION, OrderStatus.DELIVERED, deliveredTime3, null),
                    new OrderTraceModel(null, 4L, 104L, restaurantId, employeeId2, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, prepStart4, null),
                    new OrderTraceModel(null, 4L, 104L, restaurantId, employeeId2, OrderStatus.IN_PREPARATION, OrderStatus.DELIVERED, deliveredTime4, null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(traces);

            List<EmployeeEfficiencyModel> efficiencies = orderTracingUseCase.calculateEmployeeEfficiencies(restaurantId);

            assertEquals(2, efficiencies.size());
            efficiencies.forEach(efficiency -> {
                if (efficiency.getEmployeeId().equals(employeeId1)) {
                    Duration duration1 = Duration.between(prepStart1, deliveredTime1);
                    Duration duration2 = Duration.between(prepStart2, deliveredTime2);
                    Duration expectedAverage = Duration.ofSeconds((duration1.getSeconds() + duration2.getSeconds()) / 2);
                    assertEquals(expectedAverage, efficiency.getAverageProcessingTime());
                    assertEquals(2, efficiency.getCompletedOrdersCount());
                } else if (efficiency.getEmployeeId().equals(employeeId2)) {
                    Duration duration3 = Duration.between(prepStart3, deliveredTime3);
                    Duration duration4 = Duration.between(prepStart4, deliveredTime4);
                    Duration expectedAverage = Duration.ofSeconds((duration3.getSeconds() + duration4.getSeconds()) / 2);
                    assertEquals(expectedAverage, efficiency.getAverageProcessingTime());
                    assertEquals(2, efficiency.getCompletedOrdersCount());
                } else {
                    fail("Unexpected employeeId in efficiencies");
                }
            });
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }

        @Test
        @DisplayName("Should return empty list if no employees have completed orders (READY or DELIVERED)")
        void shouldReturnEmptyListIfNoCompletedOrdersByEmployees() {
            Long restaurantId = 201L;
            List<OrderTraceModel> tracesWithoutCompletion = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, 301L, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null),
                    new OrderTraceModel(null, 2L, 102L, restaurantId, null, OrderStatus.IN_PREPARATION, OrderStatus.READY, now.plusMinutes(10), null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(tracesWithoutCompletion);

            List<EmployeeEfficiencyModel> efficiencies = orderTracingUseCase.calculateEmployeeEfficiencies(restaurantId);

            assertTrue(efficiencies.isEmpty());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }

        @Test
        @DisplayName("Should ignore traces without IN_PREPARATION and a completion status (READY or DELIVERED)")
        void shouldIgnoreIncompleteEmployeeOrders() {
            Long restaurantId = 201L;
            Long employeeId = 301L;
            List<OrderTraceModel> incompleteTraces = List.of(
                    new OrderTraceModel(null, 1L, 101L, restaurantId, employeeId, OrderStatus.PENDING, OrderStatus.IN_PREPARATION, now, null),
                    new OrderTraceModel(null, 1L, 101L, restaurantId, employeeId, OrderStatus.IN_PREPARATION, OrderStatus.PENDING, now.plusMinutes(5), null)
            );

            when(tracingPersistencePort.findByRestaurantId(restaurantId)).thenReturn(incompleteTraces);

            List<EmployeeEfficiencyModel> efficiencies = orderTracingUseCase.calculateEmployeeEfficiencies(restaurantId);

            assertTrue(efficiencies.isEmpty());
            verify(tracingPersistencePort).findByRestaurantId(restaurantId);
        }
    }

}
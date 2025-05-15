package com.foodquart.microservicetraceability.domain.usecase;

import com.foodquart.microservicetraceability.domain.api.IOrderTracingServicePort;
import com.foodquart.microservicetraceability.domain.model.EmployeeEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import com.foodquart.microservicetraceability.domain.spi.IOrderTracingPersistencePort;
import com.foodquart.microservicetraceability.domain.spi.ISecurityContextPort;
import com.foodquart.microservicetraceability.domain.util.OrderStatus;
import com.foodquart.microservicetraceability.domain.util.TimePair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.foodquart.microservicetraceability.domain.util.ValidationUtil.*;

public class OrderTracingUseCase implements IOrderTracingServicePort {

    private final IOrderTracingPersistencePort tracingPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public OrderTracingUseCase(IOrderTracingPersistencePort tracingPersistencePort, ISecurityContextPort securityContextPort) {
        this.tracingPersistencePort = tracingPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public OrderTraceModel recordOrderStatusChange(OrderTraceModel orderTraceModel) {
        validationOrderAndCustomer(orderTraceModel.getOrderId(), orderTraceModel.getCustomerId());
        validationStatus(orderTraceModel.getNewStatus());

        orderTraceModel.setTimestamp(LocalDateTime.now());

        return tracingPersistencePort.saveTrace(orderTraceModel);
    }

    @Override
    public List<OrderTraceModel> getOrderTracesByCustomer(Long orderId) {
        Long customerId = securityContextPort.getCurrentUserId();

        validationOrderAndCustomer(orderId, customerId);

        return tracingPersistencePort.findByCustomerIdAndOrderId(customerId, orderId);
    }

    @Override
    public List<OrderEfficiencyModel> calculateOrderEfficiencies(Long restaurantId) {
        return tracingPersistencePort.findByRestaurantId(restaurantId).stream()
                .collect(Collectors.groupingBy(OrderTraceModel::getOrderId))
                .entrySet().stream()
                .map(entry -> {
                    Optional<TimePair> times = getStartAndEndTimes(
                            entry.getValue(),
                            OrderStatus.PENDING
                    );
                    return times.map(t -> OrderEfficiencyModel.builder()
                                    .orderId(entry.getKey())
                                    .processingTime(Duration.between(t.start(), t.end()))
                                    .build())
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<EmployeeEfficiencyModel> calculateEmployeeEfficiencies(Long restaurantId) {
        return tracingPersistencePort.findByRestaurantId(restaurantId).stream()
                .filter(t -> t.getEmployeeId() != null)
                .collect(Collectors.groupingBy(OrderTraceModel::getEmployeeId))
                .entrySet().stream()
                .map(entry -> {
                    Map<Long, List<OrderTraceModel>> byOrder = entry.getValue().stream()
                            .collect(Collectors.groupingBy(OrderTraceModel::getOrderId));

                    List<Duration> durations = byOrder.values().stream()
                            .map(traces -> getStartAndEndTimes(
                                    traces,
                                    OrderStatus.IN_PREPARATION
                            ))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(t -> Duration.between(t.start(), t.end()))
                            .toList();

                    if (durations.isEmpty()) return null;

                    Duration average = calculateAverageDuration(durations);

                    return EmployeeEfficiencyModel.builder()
                            .employeeId(entry.getKey())
                            .averageProcessingTime(average)
                            .completedOrdersCount((long) durations.size())
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(EmployeeEfficiencyModel::getAverageProcessingTime))
                .toList();
    }

}

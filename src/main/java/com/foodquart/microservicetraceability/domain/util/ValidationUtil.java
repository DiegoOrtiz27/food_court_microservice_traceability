package com.foodquart.microservicetraceability.domain.util;

import com.foodquart.microservicetraceability.domain.exception.DomainException;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.foodquart.microservicetraceability.domain.util.TracingMessages.*;

public class ValidationUtil {

    public static Optional<TimePair> getStartAndEndTimes(
            List<OrderTraceModel> traces, OrderStatus startStatus) {
        Optional<LocalDateTime> start = getStatusTimestamp(traces, startStatus);
        Optional<LocalDateTime> end = getStatusTimestamp(traces, OrderStatus.DELIVERED);
        return start.isPresent() && end.isPresent()
                ? Optional.of(new TimePair(start.get(), end.get()))
                : Optional.empty();
    }


    public static Optional<LocalDateTime> getStatusTimestamp(List<OrderTraceModel> traces, OrderStatus status) {
        return traces.stream()
                .filter(t -> t.getNewStatus() == status)
                .map(OrderTraceModel::getTimestamp)
                .findFirst();
    }

    public static Duration calculateAverageDuration(List<Duration> durations) {
        long averageSeconds = (long) durations.stream()
                .mapToLong(Duration::getSeconds)
                .average()
                .orElse(0);
        return Duration.ofSeconds(averageSeconds);
    }

    public static void validationOrderAndCustomer(Long orderId, Long customerId) {
        if (orderId == null) {
            throw new DomainException(ORDER_ID_REQUIRED);
        }
        if (customerId == null) {
            throw new DomainException(CUSTOMER_ID_REQUIRED);
        }
    }

    public static void validationStatus(OrderStatus status) {
        if (status == null) {
            throw new DomainException(STATUS_REQUIRED);
        }
    }

    ValidationUtil() {
        throw new IllegalStateException("Utility class");
    }

}

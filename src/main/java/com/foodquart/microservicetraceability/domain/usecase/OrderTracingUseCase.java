package com.foodquart.microservicetraceability.domain.usecase;

import com.foodquart.microservicetraceability.domain.api.IOrderTracingServicePort;
import com.foodquart.microservicetraceability.domain.exception.DomainException;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;
import com.foodquart.microservicetraceability.domain.spi.IOrderTracingPersistencePort;
import com.foodquart.microservicetraceability.domain.spi.ISecurityContextPort;

import java.time.LocalDateTime;
import java.util.List;

import static com.foodquart.microservicetraceability.domain.util.TracingMessages.*;

public class OrderTracingUseCase implements IOrderTracingServicePort {

    private final IOrderTracingPersistencePort tracingPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public OrderTracingUseCase(IOrderTracingPersistencePort tracingPersistencePort, ISecurityContextPort securityContextPort) {
        this.tracingPersistencePort = tracingPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public OrderTraceModel recordOrderStatusChange(OrderTraceModel orderTraceModel) {
        if (orderTraceModel.getOrderId() == null) {
            throw new DomainException(ORDER_ID_REQUIRED);
        }
        if (orderTraceModel.getCustomerId() == null) {
            throw new DomainException(CUSTOMER_ID_REQUIRED);
        }
        if (orderTraceModel.getNewStatus() == null) {
            throw new DomainException(STATUS_REQUIRED);
        }

        orderTraceModel.setTimestamp(LocalDateTime.now());

        return tracingPersistencePort.saveTrace(orderTraceModel);
    }

    @Override
    public List<OrderTraceModel> getOrderTracesByCustomer(Long orderId) {
        Long customerId = securityContextPort.getCurrentUserId();

        if (customerId == null) {
            throw new DomainException(CUSTOMER_ID_REQUIRED);
        }
        if (orderId == null) {
            throw new DomainException(ORDER_ID_REQUIRED);
        }

        return tracingPersistencePort.findByCustomerIdAndOrderId(customerId, orderId);
    }
}

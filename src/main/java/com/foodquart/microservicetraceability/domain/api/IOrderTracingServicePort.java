package com.foodquart.microservicetraceability.domain.api;

import com.foodquart.microservicetraceability.domain.model.EmployeeEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderEfficiencyModel;
import com.foodquart.microservicetraceability.domain.model.OrderTraceModel;

import java.util.List;

public interface IOrderTracingServicePort {

    OrderTraceModel recordOrderStatusChange(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> getOrderTracesByCustomer(Long orderId);

    List<OrderEfficiencyModel> calculateOrderEfficiencies(Long restaurantId);

    List<EmployeeEfficiencyModel> calculateEmployeeEfficiencies(Long restaurantId);

}

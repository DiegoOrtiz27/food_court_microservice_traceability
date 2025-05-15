package com.foodquart.microservicetraceability.infrastructure.rest;

import com.foodquart.microservicetraceability.application.dto.request.OrderTraceRequest;
import com.foodquart.microservicetraceability.application.dto.response.EmployeeEfficiencyResponse;
import com.foodquart.microservicetraceability.application.dto.response.OrderEfficiencyResponse;
import com.foodquart.microservicetraceability.application.dto.response.OrderTraceResponse;
import com.foodquart.microservicetraceability.application.handler.ITracingHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.foodquart.microservicetraceability.infrastructure.documentation.ApiDocumentationConstants.*;
import static com.foodquart.microservicetraceability.infrastructure.documentation.ResponseCode.*;

@RestController
@RequestMapping("/api/v1/tracing")
@RequiredArgsConstructor
public class TracingRestController {

    private final ITracingHandler tracingHandler;

    @PostMapping("/")
    @Operation(summary = RECORD_TRACE_SUMMARY,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = RECORD_TRACE_DESC,
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderTraceRequest.class))
            )
    )
    @ApiResponse(responseCode = CODE_201, description = RECORD_TRACE_SUCCESS,
            content = @Content(schema = @Schema(implementation = OrderTraceResponse.class)))
    @ApiResponse(responseCode = CODE_400, description = RESPONSE_400, content = @Content)
    @ApiResponse(responseCode = CODE_401, description = RESPONSE_401, content = @Content)
    @ApiResponse(responseCode = CODE_403, description = RESPONSE_403, content = @Content)
    public ResponseEntity<OrderTraceResponse> recordTrace(@RequestBody OrderTraceRequest orderTraceRequest) {
        OrderTraceResponse response = tracingHandler.recordOrderStatusChange(orderTraceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customer/order/{orderId}")
    @Operation(summary = GET_TRACES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = GET_TRACES_SUCCESS,
            content = @Content(schema = @Schema(implementation = OrderTraceResponse[].class)))
    @ApiResponse(responseCode = CODE_404, description = RESPONSE_404, content = @Content)
    public ResponseEntity<List<OrderTraceResponse>> getOrderTraces(@PathVariable Long orderId) {
        return ResponseEntity.ok(tracingHandler.getOrderTraces(orderId));
    }

    @GetMapping("/restaurant/{restaurantId}/order-efficiencies")
    @Operation(summary = ORDER_EFFICIENCIES_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = ORDER_EFFICIENCIES_SUCCESS,
            content = @Content(schema = @Schema(implementation = OrderEfficiencyResponse[].class)))
    @ApiResponse(responseCode = CODE_404, description = RESPONSE_404, content = @Content)
    public ResponseEntity<List<OrderEfficiencyResponse>> getOrderEfficiencies(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(tracingHandler.getOrderEfficiencies(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/employee-efficiency")
    @Operation(summary = EMPLOYEE_EFFICIENCY_SUMMARY)
    @ApiResponse(responseCode = CODE_200, description = EMPLOYEE_EFFICIENCY_SUCCESS,
            content = @Content(schema = @Schema(implementation = EmployeeEfficiencyResponse[].class)))
    @ApiResponse(responseCode = CODE_404, description = RESPONSE_404, content = @Content)
    public ResponseEntity<List<EmployeeEfficiencyResponse>> getEmployeeEfficiencyRanking(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(tracingHandler.getEmployeeEfficiencyRanking(restaurantId));
    }
}
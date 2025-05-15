package com.foodquart.microservicetraceability.infrastructure.rest;

import com.foodquart.microservicetraceability.application.dto.request.OrderTraceRequest;
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

@RestController
@RequestMapping("/api/v1/tracing")
@RequiredArgsConstructor
public class TracingRestController {

    private final ITracingHandler tracingHandler;

    @Operation(summary = "Creates a new trace record for an order status transition",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order trace details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderTraceRequest.class))
            )
    )
    @ApiResponse(responseCode = "201", description = "Trace record created successfully", content = @Content(schema = @Schema(implementation = OrderTraceResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", content = @Content)
    @PostMapping("/")
    public ResponseEntity<OrderTraceResponse> recordTrace(@RequestBody OrderTraceRequest orderTraceRequest) {
        OrderTraceResponse response = tracingHandler.recordOrderStatusChange(orderTraceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Retrieves all status change traces for a specific order")
    @ApiResponse(responseCode = "200", description = "List of order traces retrieved successfully", content = @Content(schema = @Schema(implementation = OrderTraceResponse[].class)))
    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    @GetMapping("/customer/order/{orderId}")
    public ResponseEntity<List<OrderTraceResponse>> getOrderTraces(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(tracingHandler.getOrderTraces(orderId));
    }
}
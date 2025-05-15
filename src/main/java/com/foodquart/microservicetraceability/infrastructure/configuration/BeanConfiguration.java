package com.foodquart.microservicetraceability.infrastructure.configuration;


import com.foodquart.microservicetraceability.domain.api.IOrderTracingServicePort;
import com.foodquart.microservicetraceability.domain.spi.IOrderTracingPersistencePort;
import com.foodquart.microservicetraceability.domain.spi.ISecurityContextPort;
import com.foodquart.microservicetraceability.domain.usecase.OrderTracingUseCase;
import com.foodquart.microservicetraceability.infrastructure.out.client.SecurityContextAdapter;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.adapter.MongoOrderTracingAdapter;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.mapper.IOrderTracingEntityMapper;
import com.foodquart.microservicetraceability.infrastructure.out.jpa.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderTraceRepository orderTraceRepository;
    private final IOrderTracingEntityMapper orderTracingEntityMapper;

    @Bean
    public IOrderTracingPersistencePort orderTracingPersistencePort() {
        return new MongoOrderTracingAdapter(
                orderTraceRepository,
                orderTracingEntityMapper);
    }

    @Bean
    public IOrderTracingServicePort orderTracingServicePort() {
        return new OrderTracingUseCase(
                orderTracingPersistencePort(),
                securityContextPort());
    }

    @Bean
    ISecurityContextPort securityContextPort() {
        return new SecurityContextAdapter();
    }
}

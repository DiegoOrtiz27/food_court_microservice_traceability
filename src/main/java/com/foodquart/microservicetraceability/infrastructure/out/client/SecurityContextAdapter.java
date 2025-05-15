package com.foodquart.microservicetraceability.infrastructure.out.client;

import com.foodquart.microservicetraceability.domain.spi.ISecurityContextPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextAdapter implements ISecurityContextPort {

    @Override
    public Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

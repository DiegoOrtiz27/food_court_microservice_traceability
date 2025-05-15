package com.foodquart.microservicetraceability.domain.spi;

public interface IJwtProviderPort {

    boolean validateToken(String token);

    Long getIdFromToken(String token);

    String getRoleFromToken(String token);
}

package com.foodquart.microservicetraceability.infrastructure.out.client;

import com.foodquart.microservicetraceability.domain.spi.IJwtProviderPort;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtProviderAdapter implements IJwtProviderPort {

    private final Key key;

    public JwtProviderAdapter(
            @Value("${security.jwt.secret}") String secretKey
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Long getIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject());
    }

    @Override
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }
}

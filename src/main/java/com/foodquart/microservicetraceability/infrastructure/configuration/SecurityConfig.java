package com.foodquart.microservicetraceability.infrastructure.configuration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.foodquart.microservicetraceability.domain.util.SecurityMessages.ACCESS_DENIED;
import static com.foodquart.microservicetraceability.domain.util.SecurityMessages.MISSING_AUTH_HEADER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String OWNER = "OWNER";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String EMPLOYEE = "EMPLOYEE";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tracing/**").hasAnyRole(EMPLOYEE, CUSTOMER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/tracing/customer/**").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/tracing/restaurant/**").hasRole(OWNER)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("""
                {
                    "code": "UNAUTHORIZED",
                    "message": "%s"
                }
            """, MISSING_AUTH_HEADER));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(String.format("""
                {
                    "code": "FORBIDDEN",
                    "message": "%s"
                }
            """, ACCESS_DENIED));
        };
    }
}

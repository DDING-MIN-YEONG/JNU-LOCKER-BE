package com.jnulocker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
public class SecurityConfig {

    @Value(("${management.endpoints.web.base-path}"))
    private String actuatorBasePath;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                requestMatcherRegistry ->
                        requestMatcherRegistry
                                .requestMatchers(CorsUtils::isPreFlightRequest)
                                .permitAll()
                                .requestMatchers( // swagger
                                        "/api-docs/**", "/swagger-resources/**", "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers( // actuator TODO: 접근 권한 설정 (ADMIN)
                                        actuatorBasePath + "/health")
                                .permitAll()
                                .anyRequest()
                                .authenticated());

        return http.build();
    }
}

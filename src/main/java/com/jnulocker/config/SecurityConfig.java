package com.jnulocker.config;

import com.jnulocker.auth.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Value(("${management.endpoints.web.base-path}"))
    private String actuatorBasePath;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(
                requestMatcherRegistry ->
                        requestMatcherRegistry
                                .requestMatchers(CorsUtils::isPreFlightRequest)
                                .permitAll()
                                .requestMatchers( // swagger
                                        "/api-docs/**", "/swagger-resources/**", "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers( // actuator TODO: 접근 권한 설정 (ADMIN)
                                        actuatorBasePath,
                                        actuatorBasePath + "/health",
                                        actuatorBasePath + "/prometheus")
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/v1/organizations",
                                        "/v1/organizations/*/departments")
                                .permitAll() // 소속대학/학과 조회 API 모든 접근 허용
                                .requestMatchers("/v1/auth/**")
                                .permitAll() // 인증 API 모든 접근 허용
                                .requestMatchers(
                                        HttpMethod.GET, "/v1/events", "/v1/events/*/lockers")
                                .hasAuthority("manager")
                                .requestMatchers(HttpMethod.POST, "/v1/events")
                                .hasAuthority("manager") // 권한이 MANAGER인 유저만 사용 가능
                                .anyRequest()
                                .authenticated());

        // JWT 필터 추가
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

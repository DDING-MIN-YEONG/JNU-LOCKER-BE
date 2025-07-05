package com.jnulocker.config;

import com.jnulocker.auth.jwt.JwtFilter;
import com.jnulocker.auth.security.CustomAccessDeniedHandler;
import com.jnulocker.auth.security.CustomAuthenticationEntryPoint;
import com.jnulocker.auth.security.CustomUserDetailsService;
import com.jnulocker.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

    @Value("${management.endpoints.web.base-path}")
    private String actuatorBasePath;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exception ->
                                exception
                                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                                        .accessDeniedHandler(customAccessDeniedHandler));

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
                                .requestMatchers(
                                        "/v1/auth/*/signup",
                                        "/v1/auth/login",
                                        "/v1/auth/reissue",
                                        "/v1/auth/send-email",
                                        "/v1/auth/verify")
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/v1/events/*/registrations/me",
                                        "/v1/events/me",
                                        "/v1/events/me/*",
                                        "/v1/announces/me",
                                        "/v1/announces/me/*")
                                .hasAuthority(Role.USER.getRole())
                                .requestMatchers(HttpMethod.POST, "/v1/events/*/registrations")
                                .hasAuthority(Role.USER.getRole())
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/v1/auth/managers/pending",
                                        "/v1/events",
                                        "/v1/events/*",
                                        "/v1/events/*/registrations",
                                        "/v1/events/*/registrations/all",
                                        "/v1/announces",
                                        "/v1/announces/*")
                                .hasAuthority(Role.MANAGER.getRole())
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/v1/events",
                                        "/v1/announces",
                                        "/v1/auth/managers/approve")
                                .hasAuthority(Role.MANAGER.getRole())
                                .requestMatchers(
                                        HttpMethod.DELETE, "/v1/events/*", "/v1/announces/*")
                                .hasAuthority(Role.MANAGER.getRole())
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/v1/events/*",
                                        "/v1/events/*/publish",
                                        "/v1/announces/*")
                                .hasAuthority(Role.MANAGER.getRole())
                                // 토큰의 role과 db의 role과 다른 문제 고려
                                .anyRequest()
                                .authenticated());

        // JWT 필터 추가
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
        return authManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

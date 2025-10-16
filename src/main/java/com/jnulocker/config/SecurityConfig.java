package com.jnulocker.config;

import com.jnulocker.auth.jwt.JwtFilter;
import com.jnulocker.auth.jwt.TokenProvider;
import com.jnulocker.auth.security.CustomAccessDeniedHandler;
import com.jnulocker.auth.security.CustomAuthenticationEntryPoint;
import com.jnulocker.auth.security.CustomUserDetailsService;
import com.jnulocker.auth.security.SecurityPaths;
import com.jnulocker.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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

    private final TokenProvider tokenProvider;
    private final SecurityPaths securityPaths;
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
                                .requestMatchers(securityPaths.getPublicPaths())
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, SecurityPaths.PUBLIC_GET_PATHS)
                                .permitAll()
                                .requestMatchers(
                                        PathRequest.toStaticResources().atCommonLocations())
                                .permitAll()

                                // USER 권한 API
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

                                // MANAGER 권한 API
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/v1/auth/managers/pending",
                                        "/v1/events",
                                        "/v1/events/*",
                                        "/v1/events/*/registrations",
                                        "/v1/events/*/registrations/all",
                                        "/v1/announces",
                                        "/v1/announces/*",
                                        "/v1/ai/documents",
                                        "/v1/ai/documents/*")
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

        http.addFilterBefore(
                new JwtFilter(tokenProvider, securityPaths),
                UsernamePasswordAuthenticationFilter.class);

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

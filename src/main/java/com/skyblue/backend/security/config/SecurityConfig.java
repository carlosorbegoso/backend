package com.skyblue.backend.security.config;

import lombok.AllArgsConstructor;

import com.skyblue.backend.security.handler.JwtWebFilter;


import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

//    private final SecurityContextRepository securityRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtWebFilter jwtWebFilter
    ) {

        return http
                .authorizeExchange()
                .pathMatchers("/api/auth/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/user/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAfter(jwtWebFilter, SecurityWebFiltersOrder.FIRST)  //Note here that the execution location must be in securityContextRepository
//                .securityContextRepository(securityRepository)
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .logout().disable()
                .build();
    }
}
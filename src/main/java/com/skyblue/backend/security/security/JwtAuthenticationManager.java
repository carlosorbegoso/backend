package com.skyblue.backend.security.security;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.skyblue.backend.security.service.JwtSigner;



//@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtSigner jwtSigner;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
//        log.info("访问 ReactiveAuthenticationManager  。。。。。。。。。。。");
        return Mono.just(authentication)
                .map(auth -> jwtSigner.parseToken(auth.getCredentials().toString()))
                .onErrorResume(e -> {
                    log.error("An error occurred while validating the token，Error type is： {}，The error message is： {}", e.getClass(), e.getMessage());
                    return Mono.empty();
                })
                .map(claims -> new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        Stream.of(claims.get(jwtSigner.getAuthoritiesTag()))
                                .peek(info -> log.info("auth permission information {}", info))
                                .map(it -> (List<Map<String, String>>)it)
                                .flatMap(it -> it.stream()
                                        .map(i -> i.get("authority"))
                                        .map(SimpleGrantedAuthority::new))
                                .collect(Collectors.toList())
                ));
    }
}
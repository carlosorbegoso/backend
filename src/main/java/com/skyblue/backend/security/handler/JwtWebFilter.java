package com.skyblue.backend.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyblue.backend.security.model.dto.SysHttpResponse;
import com.skyblue.backend.security.service.JwtSigner;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@AllArgsConstructor
public class JwtWebFilter implements WebFilter {

    private final JwtSigner jwtSigner;
    private final ReactiveRedisTemplate<String, String> reactorTemplate;

    protected Mono<Void> writeErrorMessage(ServerHttpResponse response, HttpStatus status, String msg) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(status);
        ObjectMapper mapper = new ObjectMapper();
        String body;
        try {
            body = mapper.writeValueAsString(new SysHttpResponse(status.value(), msg, null));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    private Mono<Authentication> checkToken(String token, String key) {
        Claims claims = jwtSigner.parseToken(token);
        Set<String> roles = Stream.of(claims.get(jwtSigner.getAuthoritiesTag()))
                .peek(info -> log.info("roles {}", info))
                .map(it -> (List<Map<String, String>>) it)
                .flatMap(it -> it
                        .stream()
                        .map(i -> i.get("authority")))
                .collect(Collectors.toSet());

        return reactorTemplate.opsForSet()
                .members(key)
                .doOnNext(i -> log.info("{}", i))
                .filter(roles::contains)
                .collectList()
                .flatMap(list -> {
                    if (list.size() > 0) {
                        return Flux.fromIterable(roles)
                                .map(SimpleGrantedAuthority::new)
                                .collectList()
                                .map(it -> new UsernamePasswordAuthenticationToken(claims.getSubject(), null, it))
                                .cast(Authentication.class);
                    } else
                        return Mono.just(new UsernamePasswordAuthenticationToken(null, null));
                });
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS)
            return chain.filter(exchange);
        String path = request.getPath().value();
        Set<String> permitPath = new HashSet<>(List.of("/api/auth/login", "/api/auth/logout"));
        if (permitPath.contains(path))
            return chain.filter(exchange);
        ServerHttpResponse response = exchange.getResponse();
        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null) {
            return this.writeErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "no token");
        } else if (!auth.startsWith(jwtSigner.getTokenPrefix())) {
            return this.writeErrorMessage(response, HttpStatus.NOT_ACCEPTABLE,
                    "token without" + jwtSigner.getTokenPrefix() + "start");
        }

        String token = auth.substring(jwtSigner.getTokenPrefix().length());
        String key = "api_" + path + "_" + request.getMethodValue().toLowerCase();

        return reactorTemplate.opsForSet().isMember("token_set", token)
                .flatMap(isMember -> {
                    if (isMember) {
                        try {
                            return checkToken(token, key)
                                    .doOnNext(info -> log.info("{}", info))
                                    .flatMap(authentication -> {
                                        if (authentication.getPrincipal() != null) {
                                            return chain.filter(exchange)
                                                    .contextWrite(ReactiveSecurityContextHolder
                                                            .withAuthentication(authentication));
                                            // .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
                                        } else {
                                            return this.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR,
                                                    "Access parsing is successful, but do not have permission");
                                        }
                                    });
                        } catch (Exception e) {
                            return this.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                        }
                    } else {
                        return this.writeErrorMessage(response, HttpStatus.UNAUTHORIZED,
                                "illegal token，The token has not been released，or has been deleted");
                    }
                });
    }
}
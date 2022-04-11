package com.skyblue.backend.security.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
@Slf4j
public class RedisService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public void saveToken (String token) {
        
        redisTemplate.opsForSet().add("token_set", token)
                .subscribe();
    }

    public Mono<Long> deleteToken (String token) {

        return redisTemplate.opsForSet().remove("token_set", token);
    }
}

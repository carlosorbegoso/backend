package com.skyblue.backend.security.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import com.skyblue.backend.security.service.SysApiService;

@Component
@AllArgsConstructor
@Slf4j
public class PrepareRedisData {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final SysApiService sysApiService;

    @PostConstruct
    @Order(2)
    public void route2Redis() {

        sysApiService.findAll()
                .flatMap(api -> {
                    String key = "api_" + api.getUrl().trim() + "_" + api.getRemark();
                    redisTemplate.delete(key);
                    if (!api.getRoles().isEmpty())
                        return redisTemplate.opsForSet()
                                .add(key, api.getRoles().toArray(new String[0]));
                    else
                        return Mono.empty();
                })
                .subscribe();

        log.info("api The information is cached in the redis database to complete ............");
    }
}

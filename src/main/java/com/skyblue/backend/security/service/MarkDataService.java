package com.skyblue.backend.security.service;

import com.skyblue.backend.tracing.models.Vehicle;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

import com.skyblue.backend.security.repository.DataChange;

@Service
@Order(0)
public class MarkDataService {

    public <T extends DataChange> Mono<T> createObj(T obj) {
        return ReactiveSecurityContextHolder.getContext()
                .map(it -> it.getAuthentication().getPrincipal())
                .switchIfEmpty(Mono.just("admin"))
                .map(it -> {
                    LocalDateTime now = LocalDateTime.now();
                    obj.setCreateBy((String) it);
                    obj.setCreateTime(now);
                    obj.setLastUpdateBy((String) it);
                    obj.setLastUpdateTime(LocalDateTime.now());
                    return obj;
                });
    }

    public <T extends DataChange> Mono<T> updateObj(T obj) {
        return ReactiveSecurityContextHolder.getContext()
                .map(it -> it.getAuthentication().getPrincipal())
                .switchIfEmpty(Mono.just("admin"))
                .map(it -> {
                    obj.setLastUpdateBy((String) it);
                    obj.setLastUpdateTime(LocalDateTime.now());
                    return obj;
                });
    }


}

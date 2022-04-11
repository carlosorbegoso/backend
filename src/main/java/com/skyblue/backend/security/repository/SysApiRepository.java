package com.skyblue.backend.security.repository;
import com.skyblue.backend.security.model.SysApi;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SysApiRepository extends ReactiveCrudRepository<SysApi, Long> {
    Flux<SysApi> findAllByUrl(String url);
    Mono<SysApi> findByName(String name);
    Mono<Long> getIdByUrl(String url);
}

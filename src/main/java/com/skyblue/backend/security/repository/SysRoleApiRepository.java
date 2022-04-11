package com.skyblue.backend.security.repository;

import com.skyblue.backend.security.model.SysRoleApi;

import org.reactivestreams.Publisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SysRoleApiRepository extends ReactiveCrudRepository<SysRoleApi, Long> {
    Flux<SysRoleApi> findByApiId(long id);
    Mono<SysRoleApi> findByRoleIdAndApiId(long roleId, long ApiId);

    Mono<Void> deleteByApiIdAndRoleId(Long apiId, Long oldRoleId);

    Mono<Void> deleteByApiId(Long apiId);
}

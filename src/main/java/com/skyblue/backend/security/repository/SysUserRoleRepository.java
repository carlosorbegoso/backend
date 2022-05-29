package com.skyblue.backend.security.repository;

import com.skyblue.backend.security.model.SysUserRole;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysUserRoleRepository extends ReactiveCrudRepository<SysUserRole, Long> {
    Flux<SysUserRole> findByUserId(long userId);

    Mono<SysUserRole> findByUserIdAndRoleId(long userId, long roleId);

    Mono<Void> deleteByUserId(Long id);

    Mono<Void> deleteByUserIdAndRoleId(Long userId, Long roleId);
}

package com.skyblue.backend.security.repository;

import com.skyblue.backend.security.model.SysUser;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface SysUserRepository extends ReactiveCrudRepository<SysUser, Long> {
    Mono<SysUser> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
}

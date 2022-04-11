package com.skyblue.backend.security.repository;

import com.skyblue.backend.security.model.SysRole;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SysRoleRepository extends ReactiveCrudRepository<SysRole, Long> {
    
    Mono<SysRole> findByName(String name);

}

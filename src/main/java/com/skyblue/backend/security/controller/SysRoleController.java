package com.skyblue.backend.security.controller;

import lombok.AllArgsConstructor;

import com.skyblue.backend.security.model.SysHttpResponse;
import com.skyblue.backend.security.model.SysRole;
import com.skyblue.backend.security.service.SysRoleService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public Mono<SysHttpResponse> save (@RequestBody SysRole role) {
        return sysRoleService.save(role)
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public Mono<SysHttpResponse> update (@RequestBody SysRole role) {
        return sysRoleService.save(role)
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @GetMapping("all")
    public Mono<SysHttpResponse> findAll () {
        return sysRoleService.findAll()
                .collectList()
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @GetMapping
    public Mono<SysHttpResponse> findByUrl (@RequestParam("name") String name) {
        return sysRoleService.findByName(name)
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public Mono<Void> delete (Long id) {
        return sysRoleService.delete(id);

    }
}

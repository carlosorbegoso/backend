package com.skyblue.backend.security.controller;

import lombok.AllArgsConstructor;

import com.skyblue.backend.security.model.SysApi;
import com.skyblue.backend.security.model.dto.SysHttpResponse;
import com.skyblue.backend.security.service.SysApiService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/url")
@AllArgsConstructor
public class SysApiController {

    private final SysApiService sysApiService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public Mono<SysHttpResponse> save(@RequestBody SysApi api) {
        return sysApiService.save(api)
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'IT')")
    public Mono<SysHttpResponse> update(@RequestBody SysApi api) {
        return sysApiService.save(api)
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @GetMapping("all")
    public Mono<SysHttpResponse> findAll() {
        return sysApiService.findAll()
                .collectList()
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @GetMapping
    public Mono<SysHttpResponse> findByUrl(@RequestParam("url") String url) {
        return sysApiService.findByUrl(url)
                .collectList()
                .map(SysHttpResponse::ok)
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx(e.getMessage(), e)));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<Void> delete(Long id) {
        return sysApiService.delete(id);
    }
}

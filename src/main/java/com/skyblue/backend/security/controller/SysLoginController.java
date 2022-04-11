package com.skyblue.backend.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import javax.naming.AuthenticationException;

import com.skyblue.backend.security.model.LoginResponse;
import com.skyblue.backend.security.model.SysApi;
import com.skyblue.backend.security.model.SysHttpResponse;
import com.skyblue.backend.security.model.SysRole;
import com.skyblue.backend.security.model.SysUser;
import com.skyblue.backend.security.model.SysUserDetails;
import com.skyblue.backend.security.service.JwtSigner;
import com.skyblue.backend.security.service.RedisService;
import com.skyblue.backend.security.service.SysApiService;
import com.skyblue.backend.security.service.SysRoleService;
import com.skyblue.backend.security.service.SysUserService;

import java.util.List;
import java.util.Map;

/**
 * @author: ffzs
 * @Date: 2020/8/27 下午2:31
 */

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class SysLoginController {
    private final PasswordEncoder password = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final SysUserService sysUserService;
    private final JwtSigner jwtSigner;
    private final RedisService redisService;
    private final SysApiService sysApiService;
    private final SysRoleService sysRoleService;


    @PostMapping("login")
    public Mono<SysHttpResponse> login (@RequestBody Map<String, String> user, ServerHttpResponse response) {
        return Mono.justOrEmpty(user.get("username"))
                .flatMap(sysUserService::findByUsername)
                .filter(it -> password.matches(user.get("password"), it.getPassword()))
                .map(it -> {
                    

                            List<String> roles = it.getRoles();
                        
                            String token = jwtSigner.generateToken(SysUserDetails
                                    .builder()
                                    .username(it.getUsername())
                                    .password(user.get("password"))
                                    .authorities(roles)
                                    .build());
                            redisService.saveToken(token);
                            return SysHttpResponse
                                    .ok("Successfully logged in", LoginResponse.fromUser(it).withToken(token));
                        }
                )
                .onErrorResume(e -> {
                    log.error("{} {}", e.getClass(), e.getMessage());
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return new SysHttpResponse(HttpStatus.UNAUTHORIZED.value(), "Login error", null);
                }));
    }

    @GetMapping("logout")
    public Mono<SysHttpResponse> logout (@RequestParam("token") String token) {
        return Mono.just(token)
                .flatMap(redisService::deleteToken)
                .flatMap(it -> Mono.just(SysHttpResponse.ok(it)))
                .onErrorResume(e -> Mono.just(SysHttpResponse.error5xx("Failed to remove token", e.getMessage())))
                .switchIfEmpty(Mono.just(SysHttpResponse.error5xx("Failed to remove token", null)));
    }


    // @PostMapping( "register")
	// public Mono<ResponseEntity<Flux<SysApi>>> register(SysApi user){
    //         Flux<SysApi> user_flux = sysApiService.findAll();

            
    //         return Mono.just(ResponseEntity.ok()
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .body(user_flux));
        
    // }

    @PostMapping( "register")
	public Mono<SysApi> registerUser(@RequestBody  SysApi user){
         
        System.out.println(user);

           return sysApiService.save(user);
        
    }

}

package com.skyblue.backend.security.service;

// import com.ffzs.webflux.system_app.utils.ReadExcelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.skyblue.backend.security.model.SysRole;
import com.skyblue.backend.security.model.SysUser;
import com.skyblue.backend.security.model.SysUserRole;
import com.skyblue.backend.security.repository.SysRoleRepository;
import com.skyblue.backend.security.repository.SysUserRepository;
import com.skyblue.backend.security.repository.SysUserRoleRepository;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
@Order(2)
public class SysUserService {

    private final PasswordEncoder password = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysRoleRepository sysRoleRepository;
    private final MarkDataService mark;


    private Mono<SysUser> addRoles (SysUser user) {

        return sysUserRoleRepository
                .findByUserId(user.getId())
                .map(SysUserRole::getRoleId)
                .flatMap(sysRoleRepository::findById)
                .map(SysRole::getName)
                .collectList()
                .map(it -> {
                    user.setRoles(it);
                    return user;
                });
    }

    public Flux<SysUser> findAll () {
        return sysUserRepository.findAll()
                .map(it->it.withPassword(null))
                .flatMap(this::addRoles);
    }

    public Mono<SysUser> findByUsername (String username) {
        return sysUserRepository.findByUsername(username)
                .flatMap(this::addRoles);
    }

    public Mono<SysUser>findById(long id){
        return sysUserRepository.findById(id);
    }

    private Mono<Long> checkRole (String role) {
        return sysRoleRepository.findByName(role)
                .switchIfEmpty(
                        mark.createObj(new SysRole(role))
                                .flatMap(sysRoleRepository::save)
                                .map(SysRole::getName)
                                .flatMap(sysRoleRepository::findByName)
                )
                .map(SysRole::getId);
    }


    private Mono<Void> checkUserRole (List<Long> roleIds, Long userId) {
        return sysUserRoleRepository
                .findByUserId(userId)
                .map(SysUserRole::getRoleId)
                .collectList()
                .flatMap(oldRoleIds -> Flux.fromIterable(roleIds)
                        .filter(roleId->!oldRoleIds.contains(roleId))
                        .flatMap(roleId -> mark.createObj(new SysUserRole(userId, roleId)))
                        .cast(SysUserRole.class)
                        .flatMap(sysUserRoleRepository::save)
                        .collectList()
                        .flatMap(it -> Flux
                                .fromIterable(oldRoleIds)
                                .filter(oldRoleId -> !roleIds.contains(oldRoleId))
                                .flatMap(oldRoleId -> sysUserRoleRepository
                                        .deleteByUserIdAndRoleId(userId, oldRoleId)
                                )
                                .collectList()
                        )
                        .then(Mono.empty())
                );
    }

    private Mono<SysUser> saveRoles (SysUser user) {
        List<String> roles = user.getRoles();
        System.out.println(roles);
        if (roles==null || roles.isEmpty()) return Mono.just(user);
        return Mono.from(
                Flux.fromIterable(roles)
                        .flatMap(this::checkRole)
                        .collectList()
                )
                .flatMap(roleIds-> this.checkUserRole(roleIds, user.getId()))
                .then(Mono.just(user));
    }


    public Mono<SysUser> insert (SysUser user) {
        return Mono.just(user)
                .map(it->it.withPassword(password.encode(user.getPassword())))
                .flatMap(sysUserRepository::save)
                .map(SysUser::getUsername)
                .flatMap(sysUserRepository::findByUsername)
                .map(it -> it.withRoles(user.getRoles()))
                .flatMap(this::saveRoles);
    }


    public Mono<SysUser> update (SysUser user) {
        return Mono.just(user)
                .map(oldUser -> {
            if (oldUser.getPassword() == null || oldUser.getPassword().equals(""))
                oldUser.setPassword(oldUser.getPassword());
            else oldUser.setPassword(password.encode(user.getPassword()));
            return oldUser
                    .withCreateBy(oldUser.getCreateBy())
                    .withCreateTime(oldUser.getCreateTime() );
        })
                .flatMap(sysUserRepository::save)
                .flatMap(this::saveRoles);
    }

    public Mono<SysUser> save (SysUser user) {

        if (user.getId() != 0) {  //
            return mark.updateObj(user)
                    .flatMap(this::insert);
        }
        else {   // id is 0 for create
            return mark.createObj(user)
                    .flatMap(this::update);
        }

    }



    public Mono<Void> deleteById (Long id) {
        return sysUserRepository.deleteById(id)
                .flatMap(it -> sysUserRoleRepository.deleteByUserId(id));
    }
}

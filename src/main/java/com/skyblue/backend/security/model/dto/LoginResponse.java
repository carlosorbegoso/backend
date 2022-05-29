package com.skyblue.backend.security.model.dto;

import com.skyblue.backend.security.model.SysUser;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class LoginResponse {
    String userid;
    String name;
    String email;
    String mobile;
    String avatar;
    List<String> roles;
    String token;

    public static LoginResponse fromUser (SysUser user) {
         return LoginResponse.builder()
                 .userid(String.valueOf(user.getId()))
                 .name(user.getUsername())
                 .email(user.getEmail())
                 .mobile(user.getMobile())
                 .avatar(user.getAvatar())
                 .roles(user.getRoles())
                 .build();
    }
}

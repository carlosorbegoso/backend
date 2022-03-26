package com.skyblue.backend.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtDto {
	private String token;
	private String bearer = "Bearer";
	private String nameUser;
	private Collection<? extends GrantedAuthority> authorities;

	public JwtDto(String token,String nameUser,Collection<?extends GrantedAuthority> authorities){
		this.token = token;
		this.nameUser = nameUser;
		this.authorities = authorities;
	}

}

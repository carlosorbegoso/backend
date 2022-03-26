package com.skyblue.backend.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class MainUser implements UserDetails {
	private  String name;
	private String nameUser;
	private String email;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public MainUser (String  name, String nameUser,String email,String password, Collection<? extends GrantedAuthority >authorities){
		this.name       = name;
		this.nameUser   = nameUser;
		this.email      = email;
		this.password   = password;
		this.authorities = authorities;
	}
	public static  MainUser buil(UserLogin user){
		List<GrantedAuthority> authorities = user.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getRoleName().name())).collect(Collectors.toList());
		return  new MainUser( user.getName(),user.getNameUser(),user.getEmail(),user.getPassword(),authorities);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return nameUser;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getName(){
		return  name;
	}
	public void setName(String name){
		this.name = name;
	}
}

package com.skyblue.backend.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class UserLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  int id;
	@NotNull
	private  String name;
	@NotNull
	@Column(unique = true)
	private String nameUser;
	@NotNull
	private String email;
	@NotNull
	@JsonIgnore
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();
	public UserLogin(){
	}
	public UserLogin(@NotNull String name, @NotNull String nameUser, @NotNull String email, @NotNull String password){
		this.name        = name;
		this.nameUser    = nameUser;
		this.email       = email;
		this.password    = password;
	}
}

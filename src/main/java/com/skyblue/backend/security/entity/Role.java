package com.skyblue.backend.security.entity;

import com.skyblue.backend.security.enums.RoleName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@Enumerated(EnumType.STRING)
	private RoleName roleName;
	public Role(){
	}
	public Role(@NotNull RoleName roleUser){
		this.roleName = roleUser;
	}
}

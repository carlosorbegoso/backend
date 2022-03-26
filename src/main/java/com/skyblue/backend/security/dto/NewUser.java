package com.skyblue.backend.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Validated
public class NewUser {
	@NotBlank
	private String name;
	@NotBlank
	private String nameUser;
	@Email
	@JsonIgnore
	private String email;
	@NotBlank
	@JsonIgnore
	private String password;
	private Set<String > roles = new HashSet<>();
}

package com.skyblue.backend.security.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class LoginUser {
	@NotBlank
	private String nameUser;
	@NotBlank
	private String password;
}

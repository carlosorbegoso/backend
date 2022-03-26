package com.skyblue.backend.security.controller;

import com.skyblue.backend.security.dto.JwtDto;
import com.skyblue.backend.security.dto.LoginUser;
import com.skyblue.backend.security.dto.NewUser;
import com.skyblue.backend.security.entity.Role;
import com.skyblue.backend.security.entity.UserLogin;
import com.skyblue.backend.security.enums.RoleName;
import com.skyblue.backend.security.jwt.JwtProvider;
import com.skyblue.backend.security.service.RoleService;
import com.skyblue.backend.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
// @CrossOrigin(origins = "https://siskyblue.herokuapp.com")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	@Autowired
	PasswordEncoder passwordEncoder ;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	JwtProvider jwtProvider;

	@PostMapping(path = "/register",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<?> register(@Valid NewUser newUser, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			return  new ResponseEntity<>("misplaced fields", HttpStatus.BAD_REQUEST);
		if(userService.existsByNameUser(newUser.getNameUser()))
			return new ResponseEntity<>("user already exists",HttpStatus.BAD_REQUEST);
		if (userService.existsByEmail(newUser.getEmail()))
				return new ResponseEntity<>("mail already exists",HttpStatus.BAD_REQUEST);
		System.out.println(passwordEncoder.encode(newUser.getPassword()));
		var user = new UserLogin(newUser.getName(), newUser.getNameUser(),newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()));
		Set<Role> roles = new HashSet<>();
		roles.add(roleService.getByRolName(RoleName.ROLE_USER).get());
		if (newUser.getRoles().contains("admin")){
			roles.add(roleService.getByRolName(RoleName.ROLE_ADMIN).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_CASHIER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_DISPATCHER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_PAINTER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_ELECTRICIAN).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_INSPECTOR).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_COUNTER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_ECONOMIST).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_TICKET).get());
		}else if(newUser.getRoles().contains("cajero")){
			roles.add(roleService.getByRolName(RoleName.ROLE_CASHIER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_USER).get());
		}else if(newUser.getRoles().contains("pintor")){
			roles.add(roleService.getByRolName(RoleName.ROLE_USER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_PAINTER).get());
			roles.add(roleService.getByRolName(RoleName.ROLE_MECHANICAL).get());

	}
		user.setRoles(roles);
		System.out.println(user.getPassword());
		userService.save(user);
		return new ResponseEntity<>( "User save", HttpStatus.CREATED);
	}

	@PostMapping(path = "/login",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<?>login(@Valid LoginUser loginUser,BindingResult bindingResult){
		boolean status = userService.existsByNameUser(loginUser.getNameUser());
		if(!status)
			return new ResponseEntity<>("invalid username or password",HttpStatus.BAD_REQUEST);
		if(bindingResult.hasErrors())
			return new ResponseEntity<>( "misplaced fields",HttpStatus.BAD_REQUEST);
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getNameUser(),loginUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt,userDetails.getUsername(),userDetails.getAuthorities());
		return new ResponseEntity<>(jwtDto,HttpStatus.OK);
	}
}

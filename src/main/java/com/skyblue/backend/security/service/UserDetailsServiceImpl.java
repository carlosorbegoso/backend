package com.skyblue.backend.security.service;

import com.skyblue.backend.security.entity.MainUser;
import com.skyblue.backend.security.entity.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {
	@Autowired
	UserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserLogin user = userService.getByNameUser(username).get();
		return MainUser.buil(user);
	}
}

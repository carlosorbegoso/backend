package com.skyblue.backend.security;

import com.skyblue.backend.security.jwt.JwtEntryPoint;
import com.skyblue.backend.security.jwt.JwtTokenFilter;
import com.skyblue.backend.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class mainSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	JwtEntryPoint jwtEntryPoint;

	@Bean
	public JwtTokenFilter jwtTokenFilter(){
		return  new JwtTokenFilter();
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}

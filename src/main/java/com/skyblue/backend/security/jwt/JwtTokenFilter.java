package com.skyblue.backend.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	JwtProvider jwtProvider;
	@Autowired
	UserDetailsService userDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getToken(req);
			if(token != null && jwtProvider.validateToken(token) ){
				String nameUser = jwtProvider.getNameUserFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(nameUser);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);

			}
		}catch (Exception e){
			res.addHeader("fail","fail");
		}
		filterChain.doFilter(req,res);
	}
	private String getToken(HttpServletRequest request){
		String  header = request.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer"))
			return  header.replace("Bearer","");
		return  null;
	}
}

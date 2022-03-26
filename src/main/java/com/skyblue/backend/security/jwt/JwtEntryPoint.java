package com.skyblue.backend.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtEntryPoint  implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
		res.setHeader("message","error in the token");
		res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Not authorized");
	}
}

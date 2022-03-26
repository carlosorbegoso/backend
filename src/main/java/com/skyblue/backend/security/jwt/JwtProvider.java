package com.skyblue.backend.security.jwt;

import com.skyblue.backend.security.entity.MainUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import java.util.Date;


@Component
public class JwtProvider {
	private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private int expiration;

	public String generateToken(Authentication authentication){
		MainUser mainUser = (MainUser) authentication.getPrincipal();
		return Jwts.builder().setSubject(mainUser.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	public  String getNameUserFromToken(String token){
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

	public  boolean validateToken(String token){
		try{
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return  true;
		}catch (MalformedJwtException e){
			logger.error("malformed token"+e);
		}catch (UnsupportedJwtException e){
			logger.error("token not supported"+e);
		}catch (ExpiredJwtException e){
			logger.error("expired token");
		}catch (IllegalArgumentException e){
			logger.error("empty token"+e);
		}catch (SignatureException e){
			logger.error("signature failure "+ e);
		}
		return  false;
	}



}

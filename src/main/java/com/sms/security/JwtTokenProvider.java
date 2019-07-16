package com.sms.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
	private static final Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);
	
	   @Value("${app.jwtSecret}")
	    private String jwtSecret;
	   
	   @Value("${app.jwtExpirationInMs}")
	    private int jwtExpirationInMs;

	   
	   public String generateToken(Authentication authentication) {
		   
		   UserPrincipal userPrincipal= (UserPrincipal) authentication.getPrincipal();
		   
		   Date now= new Date();
		   Date expiryDate= new Date(now.getTime()+jwtExpirationInMs);
		   
		   return Jwts.builder()
				   .setSubject(Long.toString(userPrincipal.getId()))
				   .setIssuedAt(new Date())
	               .setExpiration(expiryDate)
	               .signWith(SignatureAlgorithm.HS512, jwtSecret)
	               .compact();
	   }

	   public Long getUserIdFromJwt(String token) {
		   
		   logger.info("Parsing UserId from Token");
		   Claims claims= Jwts.parser()
				   .setSigningKey(jwtSecret)
				   .parseClaimsJws(token)
				   .getBody();
		   
		   logger.info("Parsed UserId:"+claims.getSubject());
		   return Long.parseLong(claims.getSubject());
				   
	   }
	   
	   
	   public boolean validateToken(String token) throws Exception 
	   {
		   
		   try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		   		}
		   catch (SignatureException e)
		   {
			logger.error("Incorrect Signature in JWT Token"); 
			e.printStackTrace();
		   }
		   
		   catch (ExpiredJwtException e)
		   {
			logger.error("Expired JWT Token");
			e.printStackTrace();
		   }
		   catch (UnsupportedJwtException e)
		   {
			logger.error("Unsupported JWT Token");
			e.printStackTrace();
		   }
		   catch (MalformedJwtException e)
		   {
			logger.error("Malformed JWT Token");
			e.printStackTrace();	
		   }
		   catch (IllegalArgumentException e)
		   {
			logger.error("Malformed JWT Token");
			e.printStackTrace();
		   }
				
			return false;
		}
	   }




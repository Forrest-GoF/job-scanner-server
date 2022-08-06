package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.auth.enumerate.RoleType;

import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {

	//TODO @Value 적용하기
	@Value("${app.auth.token-expiry}")
	private String expiry;

	//TODO @Value 적용하기
	private final Key key;

	public AuthTokenProvider(@Value("${app.auth.token-secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public AuthToken createToken(String id, RoleType roleType, String expiry) {
		Date expiryDate = new Date(System.currentTimeMillis() + Long.parseLong(expiry));
		return new AuthToken(id, roleType, expiryDate, key);
	}

	public AuthToken createUserAppToken(String id) {
		return createToken(id, RoleType.USER, expiry);
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}
}

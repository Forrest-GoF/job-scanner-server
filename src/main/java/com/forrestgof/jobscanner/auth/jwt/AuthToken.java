package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

@Getter
public class AuthToken {

	private final String token;
	private final Key key;

	public AuthToken(String socialId, String role, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(socialId, role, expiry);
	}

	private String createAuthToken(String socialId, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(socialId)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}
}

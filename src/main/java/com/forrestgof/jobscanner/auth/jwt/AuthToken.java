package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Date;

import com.forrestgof.jobscanner.auth.enumerate.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AuthToken {

	private final String token;
	private final Key key;

	private static final String AUTHORITIES_KEY = "role";

	public AuthToken(String socialId, RoleType roleType, Date expiry, Key key) {
		String role = roleType.toString();
		log.trace(role);

		this.key = key;
		this.token = createAuthToken(socialId, role, expiry);
	}

	private String createAuthToken(String socialId, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(socialId) //토큰 제목 설정
			.claim(AUTHORITIES_KEY, role) //사용자 정의 클레임
			.signWith(key, SignatureAlgorithm.HS256) //암호화 복호화 Key
			.setExpiration(expiry) //토큰 만료 시간 설정
			.compact(); //토큰 생성
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			log.info("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
		}
		return null;
	}
}

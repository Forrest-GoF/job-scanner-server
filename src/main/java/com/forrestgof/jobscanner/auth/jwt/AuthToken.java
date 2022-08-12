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

	private final String appToken;
	private final String refreshToken;
	private final Key appKey;
	private final Key refreshKey;

	private static final String AUTHORITIES_KEY = "role";

	public AuthToken(String socialId, RoleType roleType,
		Date appTokenExpiry, Date refreshTokenExpiry,
		Key appKey, Key refreshKey) {

		String role = roleType.toString();

		this.appKey = appKey;
		this.refreshKey = refreshKey;
		this.appToken = createAppToken(socialId, role, appTokenExpiry);
		this.refreshToken = createRefreshToken(role, refreshTokenExpiry);
	}

	//TODO app 토큰에 유저 정보 대신 UUID 담기
	private String createAppToken(String socialId, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(socialId) //토큰 제목 설정
			.claim(AUTHORITIES_KEY, role) //사용자 정의 클레임
			.signWith(appKey, SignatureAlgorithm.HS256) //암호화 복호화 Key
			.setExpiration(expiry) //토큰 만료 시간 설정
			.compact(); //토큰 생성
	}

	private String createRefreshToken(String role, Date expiry) {
		return Jwts.builder()
			.claim(AUTHORITIES_KEY, role)
			.signWith(refreshKey, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validateApp() {
		return this.getAppTokenClaims() != null;
	}

	public boolean validateRefresh() {
		return this.getRefreshTokenClaims() != null;
	}

	public Claims getAppTokenClaims() {
		return getTokenClaims(this.appToken, this.appKey);
	}

	public Claims getExpiredAppTokenClaims() {
		try {
			Jwts.parserBuilder()
				.setSigningKey(this.appKey)
				.build()
				.parseClaimsJws(this.appToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
		return null;
	}

	public Claims getRefreshTokenClaims() {
		return getTokenClaims(this.refreshToken, this.refreshKey);
	}

	private Claims getTokenClaims(String token, Key key) {
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

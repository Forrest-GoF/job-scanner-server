package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Date;

import com.forrestgof.jobscanner.auth.RoleType;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

	public AuthToken(String appTokenUuid, String refreshTokenUuid,
		RoleType roleType,
		Date appTokenExpiry, Date refreshTokenExpiry,
		Key appKey, Key refreshKey) {

		String role = roleType.toString();

		this.appKey = appKey;
		this.refreshKey = refreshKey;
		this.appToken = createToken(appTokenUuid, role, this.appKey, appTokenExpiry);
		this.refreshToken = createToken(refreshTokenUuid, role, this.refreshKey, refreshTokenExpiry);
	}

	private String createToken(String uuid, String role, Key key, Date expiry) {
		return Jwts.builder()
			.claim("jti", uuid)
			.claim(AUTHORITIES_KEY, role)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public Claims getAppTokenClaims() {
		return getTokenClaims(this.appToken, this.appKey);
	}

	public Claims getExpiredAppTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(this.appKey)
				.build()
				.parseClaimsJws(this.appToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		} catch (SecurityException | JwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
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
		} catch (SecurityException | JwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
	}
}

package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.auth.RoleType;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {

	private final String appTokenExpiry;
	private final String refreshTokenExpiry;

	private final Key appKey;
	private final Key refreshKey;
	private static final String AUTHORITIES_KEY = "role";

	public AuthTokenProvider(AuthProperties authProperties) {
		AuthProperties.Jwt jwt = authProperties.jwt();

		this.appTokenExpiry = jwt.appTokenExpiry();
		this.refreshTokenExpiry = jwt.refreshTokenExpiry();

		String appSecretKey = jwt.appTokenSecret();
		String refreshSecretKey = jwt.refreshTokenSecret();
		this.appKey = Keys.hmacShaKeyFor(appSecretKey.getBytes());
		this.refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
	}

	public AuthToken createToken(
		String appTokenUuid,
		String refreshTokenUuid,
		RoleType roleType,
		String appTokenExpiry,
		String refreshTokenExpiry
	) {
		Date appTokenExpiryDate = new Date(System.currentTimeMillis() + Long.parseLong(appTokenExpiry));
		Date refreshTokenExpiryDate = new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiry));
		return new AuthToken(appTokenUuid, refreshTokenUuid, roleType, appTokenExpiryDate, refreshTokenExpiryDate,
			this.appKey, this.refreshKey);
	}

	public AuthToken createUserAuthToken(String appTokenUuid, String refreshTokenUuid) {
		return createToken(
			appTokenUuid,
			refreshTokenUuid,
			RoleType.USER,
			this.appTokenExpiry,
			this.refreshTokenExpiry);
	}

	public AuthToken convertAuthToken(String appToken, String refreshToken) {
		return new AuthToken(
			appToken,
			refreshToken,
			this.appKey,
			this.refreshKey);
	}

	public AuthToken convertAuthToken(String appToken) {
		return new AuthToken(
			appToken,
			appToken,
			this.appKey,
			this.refreshKey);
	}

	public Authentication getAuthentication(AuthToken authToken) {

		Claims claims = authToken.getAppTokenClaims();
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(
			claims.get("jti", String.class),
			"",
			authorities);

		return new UsernamePasswordAuthenticationToken(
			principal,
			authToken,
			authorities);
	}
}

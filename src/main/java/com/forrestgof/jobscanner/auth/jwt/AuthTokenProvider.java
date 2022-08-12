package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.auth.RoleType;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {

	@Value("${app.auth.app-token-expiry}")
	private String appTokenExpiry;
	@Value("${app.auth.refresh-token-expiry}")
	private String refreshTokenExpiry;

	private final Key appKey;
	private final Key refreshKey;
	private static final String AUTHORITIES_KEY = "role";

	public AuthTokenProvider(
		@Value("${app.auth.app-token-secret}") String appSecretKey,
		@Value("${app.auth.refresh-token-secret}") String refreshSecretKey) {

		this.appKey = Keys.hmacShaKeyFor(appSecretKey.getBytes());
		this.refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
	}

	public AuthToken createToken(String appTokenUuid, String refreshTokenUuid, RoleType roleType,
		String appTokenExpiry, String refreshTokenExpiry) {

		Date appTokenExpiryDate = new Date(System.currentTimeMillis() + Long.parseLong(appTokenExpiry));
		Date refreshTokenExpiryDate = new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiry));
		return new AuthToken(appTokenUuid, refreshTokenUuid, roleType, appTokenExpiryDate, refreshTokenExpiryDate,
			appKey, refreshKey);
	}

	public AuthToken createUserAuthToken(String appTokenUuid, String refreshTokenUuid) {
		return createToken(appTokenUuid, refreshTokenUuid, RoleType.USER, appTokenExpiry, refreshTokenExpiry);
	}

	public AuthToken convertAuthToken(String appToken, String refreshToken) {
		return new AuthToken(appToken, refreshToken, appKey, refreshKey);
	}

	public AuthToken convertAuthToken(String appToken) {
		return new AuthToken(appToken, appToken, appKey, refreshKey);
	}

	public Authentication getAuthentication(AuthToken authToken) {

		if (authToken.validateApp()) {

			Claims claims = authToken.getAppTokenClaims();
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			User pricipal = new User(claims.get("jti", String.class), "", authorities);

			return new UsernamePasswordAuthenticationToken(pricipal, authToken, authorities);
		} else {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
	}
}

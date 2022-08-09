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

import com.forrestgof.jobscanner.auth.enumerate.RoleType;
import com.forrestgof.jobscanner.auth.exception.TokenValidFailedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {

	@Value("${app.auth.token-expiry}")
	private String expiry;

	private final Key key;
	private static final String AUTHORITIES_KEY = "role";

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

	public Authentication getAuthentication(AuthToken authToken) {

		if (authToken.validate()) {

			Claims claims = authToken.getTokenClaims();
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			User pricipal = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(pricipal, authToken, authorities);
		} else {
			throw new TokenValidFailedException();
		}
	}
}

package com.forrestgof.jobscanner.auth.jwt;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.forrestgof.jobscanner.auth.exception.AuthException;

public class JwtHeaderUtil {

	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final static String TOKEN_PREFIX = "Bearer ";
	private final static String HEADER_REFRESH_TOKEN = "refresh-token";
	private final static String CODE = "code";

	public static String getCode(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(CODE))
			.orElseThrow(() -> new AuthException("The header does not contain code"));
	}

	public static String getAccessToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HEADER_AUTHORIZATION);

		if (headerValue == null || !headerValue.startsWith(TOKEN_PREFIX)) {
			throw new AuthException("The header does not contain app token or has an invalid format");
		}

		return headerValue.substring(TOKEN_PREFIX.length());
	}

	public static String getRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(HEADER_REFRESH_TOKEN))
			.orElseThrow(() -> new AuthException("The header does not contain refresh token"));
	}
}

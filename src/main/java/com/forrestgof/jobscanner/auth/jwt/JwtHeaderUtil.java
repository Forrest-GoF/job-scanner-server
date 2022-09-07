package com.forrestgof.jobscanner.auth.jwt;

import javax.servlet.http.HttpServletRequest;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

public class JwtHeaderUtil {

	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final static String TOKEN_PREFIX = "Bearer ";
	private final static String HEADER_REFRESH_TOKEN = "refresh-token";
	private final static String CODE = "code";

	public static String getCode(HttpServletRequest request) {
		if (request.getHeader(CODE) == null) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
		return request.getHeader(CODE);
	}

	public static String getAccessToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HEADER_AUTHORIZATION);

		if (headerValue == null || !headerValue.startsWith(TOKEN_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}

		return headerValue.substring(TOKEN_PREFIX.length());
	}

	public static String getRefreshToken(HttpServletRequest request) {
		if (request.getHeader(HEADER_REFRESH_TOKEN) == null) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
		return request.getHeader(HEADER_REFRESH_TOKEN);
	}
}

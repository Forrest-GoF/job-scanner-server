package com.forrestgof.jobscanner.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.auth.service.KakaoAuthService;
import com.forrestgof.jobscanner.common.dto.ApiResponse;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final KakaoAuthService kakaoAuthService;
	private final AuthTokenProvider authTokenProvider;
	private final AuthService authService;

	@PostMapping("/login/kakao")
	public ResponseEntity<ApiResponse> kakaoAuthRequest(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);

		try {
			return ApiResponse.toResponseEntity(kakaoAuthService.login(new AuthRequest((accessToken))));
		} catch (CustomException e) {
			return ApiResponse.toResponseEntity(e.getErrorCode());
		}
	}

	@PostMapping("/refresh/kakao")
	public ResponseEntity<ApiResponse> refreshToken(
		@RequestHeader Map<String, String> header,
		HttpServletResponse response) {

		String TOKEN_PREFIX = "Bearer ";
		String appToken = header.get("authorization").substring(TOKEN_PREFIX.length());
		String refreshToken = header.get("refresh-token");

		AuthToken authToken = authTokenProvider.convertAuthToken(appToken, refreshToken);

		if (!authToken.isValidRefresh()) {
			return ApiResponse.toResponseEntity(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}

		AuthResponse authResponse = authService.updateToken(authToken);
		if (authResponse == null) {
			return ApiResponse.toResponseEntity(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}

		return ApiResponse.toResponseEntity(authResponse);
	}
}

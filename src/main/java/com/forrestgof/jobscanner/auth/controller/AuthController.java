package com.forrestgof.jobscanner.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.dto.ApiResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.auth.service.KakaoAuthService;

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

	@GetMapping("/kakao")
	public ResponseEntity<AuthResponse> kakaoAuthRequest(HttpServletRequest request, HttpServletResponse response) {

		String accessToken = JwtHeaderUtil.getAccessToken(request);
		AuthResponse appTokens = kakaoAuthService.login(new AuthRequest(accessToken));
		if (appTokens == null) {
			return ApiResponse.forbidden(null);
		}

		response.setHeader("app-token", appTokens.getAppToken());
		response.setHeader("refresh-token", appTokens.getRefreshToken());
		return ApiResponse.success(null);
	}

	@GetMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
		@RequestHeader Map<String, String> header,
		HttpServletResponse response) {

		String appToken = header.get("app-token");
		String refreshToken = header.get("refresh-token");

		AuthToken authToken = authTokenProvider.convertAuthToken(appToken, refreshToken);
		if (!authToken.validateRefresh()) {
			return ApiResponse.forbidden(null);
		}

		AuthResponse authResponse = authService.updateToken(authToken);
		if (authResponse == null) {
			return ApiResponse.forbidden(null);
		}

		response.setHeader("appToken", authResponse.getAppToken());
		return ApiResponse.success(null);
	}
}

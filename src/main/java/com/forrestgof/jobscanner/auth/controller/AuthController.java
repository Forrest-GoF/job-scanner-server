package com.forrestgof.jobscanner.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.session.dto.LoginResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthTokenService;
import com.forrestgof.jobscanner.common.util.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AuthTokenProvider authTokenProvider;
	private final AuthTokenService authTokenService;

	@PostMapping("/signup/kakao")
	public ResponseEntity signup(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		authService.signup(accessToken);
		return CustomResponse.success();
	}

	@PostMapping("/login/kakao")
	public ResponseEntity<LoginResponse> kakaoAuthRequest(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		return CustomResponse.success(authService.login(accessToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthRefreshResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		String refreshToken = JwtHeaderUtil.getRefreshToken(request);
		AuthToken authToken = authTokenProvider.convertAuthToken(appToken, refreshToken);
		return CustomResponse.success(authTokenService.refreshAuthToken(authToken));
	}
}

package com.forrestgof.jobscanner.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthTokenService;
import com.forrestgof.jobscanner.auth.service.KakaoAuthService;
import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.member.dto.MemberResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthTokenProvider authTokenProvider;
	private final AuthTokenService authTokenService;
	private final KakaoAuthService kakaoAuthService;

	@PostMapping("/signup/kakao")
	public ResponseEntity<MemberResponse> signup(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		return CustomResponse.success(kakaoAuthService.signup(accessToken));
	}

	@PostMapping("/login/kakao")
	public ResponseEntity<AuthResponse> kakaoAuthRequest(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		return CustomResponse.success(kakaoAuthService.login(accessToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		String refreshToken = JwtHeaderUtil.getRefreshToken(request);
		AuthToken authToken = authTokenProvider.convertAuthToken(appToken, refreshToken);
		return CustomResponse.success(authTokenService.refreshAuthToken(authToken));
	}
}

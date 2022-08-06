package com.forrestgof.jobscanner.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PostMapping("/kakao")
	public ResponseEntity<AuthResponse> kakaoAuthRequest(@RequestBody AuthRequest authRequest) {
		return ApiResponse.success(kakaoAuthService.login(authRequest));
	}

	@GetMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		log.trace(appToken);
		AuthToken authToken = authTokenProvider.convertAuthToken(appToken);
		if (!authToken.validate()) { // 형식에 맞지 않는 token
			return ApiResponse.forbidden(null);
		}

		AuthResponse authResponse = authService.updateToken(authToken);
		if (authResponse == null) { // token 만료
			return ApiResponse.forbidden(null);
		}

		return ApiResponse.success(authResponse);
	}
}

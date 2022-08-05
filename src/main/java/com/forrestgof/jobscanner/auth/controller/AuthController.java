package com.forrestgof.jobscanner.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	@PostMapping("/kakao")
	public AuthResponse kakaoAuthRequest(@RequestBody AuthRequest authRequest) {
		log.trace("authRequest={}", authRequest);
		return kakaoAuthService.login(authRequest);
	}

}

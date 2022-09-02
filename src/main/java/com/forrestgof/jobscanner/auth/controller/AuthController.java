package com.forrestgof.jobscanner.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.dto.AuthLoginResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.auth.social.KakaoTokenValidator;
import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final KakaoTokenValidator kakaoTokenValidator;
	private final AuthService authService;

	@PostMapping("/signup/kakao")
	public ResponseEntity signup(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		Member member = kakaoTokenValidator.getMemberFromAccessToken(accessToken);
		memberService.save(member);
		return CustomResponse.success();
	}

	@PostMapping("/login/kakao")
	public ResponseEntity<AuthLoginResponse> kakaoAuthRequest(HttpServletRequest request) {
		String accessToken = JwtHeaderUtil.getAccessToken(request);
		Member member = kakaoTokenValidator.getMemberFromAccessToken(accessToken);
		Member findMember = memberService.findByEmail(member.getEmail());
		return CustomResponse.success(authService.login(findMember));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthRefreshResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		String refreshToken = JwtHeaderUtil.getRefreshToken(request);
		return CustomResponse.success(authService.refreshToken(appToken, refreshToken));
	}
}

package com.forrestgof.jobscanner.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.dto.MemberResponse;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.session.service.SessionService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoAuthService extends AuthService {

	private KakaoAuthService(
		WebClient webClient,
		AuthTokenProvider authTokenProvider,
		MemberService memberService,
		SessionService sessionService) {
		super(webClient, authTokenProvider, memberService, sessionService);
	}

	@Override
	protected KakaoUserResponse getUserResponse(String accessToken) {
		return webClient.get()
			.uri("https://kapi.kakao.com/v2/user/me")
			.headers(h -> h.setBearerAuth(accessToken))
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response
				-> Mono.error(
				new CustomException("Social Access Token is unauthorized", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(HttpStatus::is5xxServerError, response
				-> Mono.error(new CustomException("Internal Server Error", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.bodyToMono(KakaoUserResponse.class)
			.block();
	}

	@Override
	protected String getEmail(String accessToken) {
		KakaoUserResponse kakaoUserResponse = getUserResponse(accessToken);
		return kakaoUserResponse.getKakaoAccount().getEmail();
	}

	@Override
	public MemberResponse signup(String accessToken) {
		String email = getEmail(accessToken);
		return super.signupByEmail(email);
	}

	@Override
	public AuthResponse login(String accessToken) {
		String email = getEmail(accessToken);
		return super.loginByEmail(email);
	}
}

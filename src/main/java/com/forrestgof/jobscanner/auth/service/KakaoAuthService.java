package com.forrestgof.jobscanner.auth.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.client.ClientKakao;
import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final ClientKakao clientKakao;
	private final AuthTokenProvider authTokenProvider;

	public AuthResponse login(AuthRequest authRequest) {
		KakaoUserResponse kakaoUserResponse = clientKakao.getUserData(authRequest.getAccessToken());
		log.trace("kakaoUserResponse={}", kakaoUserResponse.toString());
		String socialId = String.valueOf(kakaoUserResponse.getId());

		AuthToken appToken = authTokenProvider.createUserAppToken(socialId);

		return AuthResponse.builder()
			.appToken(appToken.getToken())
			.build();
	}
}

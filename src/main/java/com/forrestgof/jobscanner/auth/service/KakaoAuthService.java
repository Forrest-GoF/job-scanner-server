package com.forrestgof.jobscanner.auth.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final AuthTokenProvider authTokenProvider;

	public AuthResponse login(AuthRequest authRequest) {
		log.trace("authRequest={}", authRequest);
		String socialId = "This is socialId";

		AuthResponse authResponse = new AuthResponse();

		AuthToken appToken = authTokenProvider.createUserAppToken(socialId);

		authResponse.setAppToken(appToken.getToken());
		return authResponse;
	}
}

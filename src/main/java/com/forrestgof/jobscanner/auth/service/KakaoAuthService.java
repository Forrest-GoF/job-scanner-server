package com.forrestgof.jobscanner.auth.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.client.ClientKakao;
import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.repository.MemoryKakaoRepsitory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final ClientKakao clientKakao;
	private final AuthTokenProvider authTokenProvider;
	private final MemoryKakaoRepsitory memoryKakaoRepsitory;

	public AuthResponse login(AuthRequest authRequest) {
		KakaoUserResponse kakaoUserResponse = clientKakao.getUserData(authRequest.getAccessToken());
		if (kakaoUserResponse == null) {
			return null;
		}

		String kakaoId = kakaoUserResponse.getId().toString();

		if (memoryKakaoRepsitory.findById(kakaoId).isEmpty()) {
			memoryKakaoRepsitory.save(kakaoUserResponse);
		}

		AuthToken appTokens = authTokenProvider.createUserAppTokens(kakaoId);

		return AuthResponse.builder()
			.appToken(appTokens.getAppToken())
			.refreshToken(appTokens.getRefreshToken())
			.build();
	}
}

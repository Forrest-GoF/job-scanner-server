package com.forrestgof.jobscanner.auth.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.session.service.SessionService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

	private final AuthTokenProvider authTokenProvider;
	private final SessionService sessionService;

	public AuthResponse updateToken(AuthToken authToken) {
		Claims appClaims = authToken.getExpiredAppTokenClaims();
		Claims refreshClaims = authToken.getRefreshTokenClaims();

		String appTokenUuid = appClaims.get("jti", String.class);
		String refreshTokenUuid = refreshClaims.get("jti", String.class);

		sessionService.validateAppTokenWithRefreshToken(appTokenUuid, refreshTokenUuid);

		AuthToken newAppToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return AuthResponse.builder()
			.appToken(newAppToken.getAppToken())
			.build();
	}
}

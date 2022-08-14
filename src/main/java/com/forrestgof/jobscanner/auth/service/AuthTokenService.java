package com.forrestgof.jobscanner.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.service.SessionService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

	private final AuthTokenProvider authTokenProvider;
	private final SessionService sessionService;

	public AuthRefreshResponse refreshAuthToken(AuthToken authToken) {
		validateAuthToken(authToken);
		AuthToken newAuthToken = createAuthToken();
		saveAuthToken(newAuthToken, authToken);
		deleteAuthToken(authToken);
		return AuthRefreshResponse.builder()
			.appToken(newAuthToken.getAppToken())
			.build();
	}

	private void validateAuthToken(AuthToken authToken) {
		Claims appClaims = authToken.getAppTokenClaimsIncludingExpired();
		Claims refreshClaims = authToken.getRefreshTokenClaims();

		String appTokenUuid = appClaims.get("jti", String.class);
		String refreshTokenUuid = refreshClaims.get("jti", String.class);

		if (!sessionService.existsByAppTokenUuidAndRefreshTokenUuid(appTokenUuid, refreshTokenUuid)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
	}

	private AuthToken createAuthToken() {
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();
		return authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);
	}

	private void saveAuthToken(AuthToken newAuthToken, AuthToken authToken) {
		Claims newAppClaims = newAuthToken.getAppTokenClaims();
		Claims appClaims = authToken.getAppTokenClaimsIncludingExpired();
		Claims refreshClaims = authToken.getRefreshTokenClaims();

		String newAppTokenUuid = newAppClaims.get("jti", String.class);
		String appTokenUuid = appClaims.get("jti", String.class);
		String refreshTokenUuid = refreshClaims.get("jti", String.class);

		Session session = sessionService.findByAppTokenUuid(appTokenUuid);

		Member member = session.getMember();
		Session newSession = Session.builder()
			.member(member)
			.appTokenUuid(newAppTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();

		sessionService.save(newSession);
	}

	private void deleteAuthToken(AuthToken authToken) {
	}
}
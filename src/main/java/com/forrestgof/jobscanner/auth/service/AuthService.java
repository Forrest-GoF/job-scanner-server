package com.forrestgof.jobscanner.auth.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.auth.repository.MemoryKakaoRepsitory;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthTokenProvider authTokenProvider;
	private final MemoryKakaoRepsitory memoryKakaoRepsitory;

	public AuthResponse updateToken(AuthToken authToken) {
		Claims claims = authToken.getTokenClaims();
		if (claims == null) {
			return null;
		}

		String id = claims.getSubject();

		AuthToken newAppToken = authTokenProvider.createUserAppToken(id);

		return AuthResponse.builder()
			.appToken(newAppToken.getToken())
			.build();
	}
}

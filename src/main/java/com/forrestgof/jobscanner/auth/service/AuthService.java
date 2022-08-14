package com.forrestgof.jobscanner.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.dto.MemberResponse;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.service.SessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AuthService<T> {

	protected final WebClient webClient;
	private final AuthTokenProvider authTokenProvider;
	private final MemberService memberService;
	private final SessionService sessionService;

	protected abstract T getUserResponse(String accessToken);

	protected abstract String getEmail(String accessToken);

	protected abstract MemberResponse signup(String accessToken);

	protected abstract AuthResponse login(String accessToken);

	protected MemberResponse signupByEmail(String email) {
		Member member = Member.builder()
			.email(email)
			.build();
		memberService.save(member);
		return MemberResponse.of(member);
	}

	protected AuthResponse loginByEmail(String email) {

		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();

		Member member = memberService.findByEmail(email);
		Session session = Session.builder()
			.member(member)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		sessionService.save(session);

		AuthToken authToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return AuthResponse.builder()
			.appToken(authToken.getAppToken())
			.refreshToken(authToken.getRefreshToken())
			.build();
	}
}

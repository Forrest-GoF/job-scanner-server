package com.forrestgof.jobscanner.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.dto.AuthLoginResponse;
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
public abstract class AuthService {

	protected final WebClient webClient;
	private final AuthTokenProvider authTokenProvider;
	private final MemberService memberService;
	private final SessionService sessionService;

	protected abstract Member getMemberFromAccessToken(String accessToken);

	protected abstract void signup(String accessToken);

	protected abstract AuthLoginResponse login(String accessToken);

	protected void signupByMember(Member member) {
		memberService.save(member);
	}

	protected AuthLoginResponse loginByEmail(String email) {
		Member findMember = memberService.findByEmail(email);
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();

		Session session = Session.builder()
			.member(findMember)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		sessionService.save(session);

		AuthToken authToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return AuthLoginResponse.builder()
			.memberResponse(MemberResponse.of(findMember))
			.appToken(authToken.getAppToken())
			.refreshToken(authToken.getRefreshToken())
			.build();
	}
}

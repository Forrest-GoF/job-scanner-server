package com.forrestgof.jobscanner.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.client.ClientKakao;
import com.forrestgof.jobscanner.auth.dto.AuthRequest;
import com.forrestgof.jobscanner.auth.dto.AuthResponse;
import com.forrestgof.jobscanner.auth.exception.InvalidTokenException;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.exception.NotFoundMemberException;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final ClientKakao clientKakao;
	private final AuthTokenProvider authTokenProvider;
	private final MemberService memberService;
	private final SessionRepository sessionRepository;

	public AuthResponse login(AuthRequest authRequest) {
		String kakaoAccessToken = authRequest.getAccessToken();
		validateToken(kakaoAccessToken);
		String email = getEmail(kakaoAccessToken);

		//임의로 회원가입은 되어 있다고 가정
		Member member = Member.builder()
			.email(email)
			.build();
		memberService.join(member);

		checkJoinedMember(email);
		member = memberService.findByEmail(email);
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();
		Session session = Session.builder()
			.member(member)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		sessionRepository.save(session);

		AuthToken authToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return AuthResponse.builder()
			.appToken(authToken.getAppToken())
			.refreshToken(authToken.getRefreshToken())
			.build();
	}

	private void validateToken(String kakaoAccessToken) {
		if (clientKakao.getUserData(kakaoAccessToken) == null) {
			throw new InvalidTokenException();
		}
	}

	private String getEmail(String kakaoAccessToken) {
		return clientKakao.getUserData(kakaoAccessToken)
			.getKakaoAccount()
			.getEmail();
	}

	private void checkJoinedMember(String email) {
		if (!memberService.existsByEmail(email)) {
			throw new NotFoundMemberException();
		}
	}
}

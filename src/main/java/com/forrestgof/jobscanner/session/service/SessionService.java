package com.forrestgof.jobscanner.session.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.dto.MemberResponse;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.dto.LoginResponse;
import com.forrestgof.jobscanner.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

	private final SessionRepository sessionRepository;
	private final MemberService memberService;
	private final AuthTokenProvider authTokenProvider;

	@Transactional
	public Session save(Session session) {
		return sessionRepository.save(session);
	}

	public Session findByAppTokenUuid(String appTokenUuid) {
		return sessionRepository.findByAppTokenUuid(appTokenUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION));
	}

	public boolean existsByAppTokenUuidAndRefreshTokenUuid(String appTokenUuid, String refreshTokenUuid) {
		return sessionRepository.existsByAppTokenUuidAndRefreshTokenUuid(appTokenUuid, refreshTokenUuid);
	}

	@Transactional
	public LoginResponse login(String email) {
		Member findMember = memberService.findByEmail(email);
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();

		Session session = Session.builder()
			.member(findMember)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		sessionRepository.save(session);

		AuthToken authToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return LoginResponse.builder()
			.memberResponse(MemberResponse.of(findMember))
			.appToken(authToken.getAppToken())
			.refreshToken(authToken.getRefreshToken())
			.build();
	}
}

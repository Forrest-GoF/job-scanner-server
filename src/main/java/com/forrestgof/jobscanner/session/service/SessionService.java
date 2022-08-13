package com.forrestgof.jobscanner.session.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

	private final SessionRepository sessionRepository;
	private final MemberService memberService;

	public Session saveWithAllArgument(String email, String appTokenUuid, String refreshTokenUuid) {
		Member findMember = memberService.findByEmail(email);
		Session session = Session.builder()
			.member(findMember)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		return sessionRepository.save(session);
	}

	public void validateAppTokenWithRefreshToken(String appTokenUuid, String refreshTokenUuid) {
		if (!sessionRepository.existsByAppTokenUuidAndRefreshTokenUuid(appTokenUuid, refreshTokenUuid)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}
	}
}

package com.forrestgof.jobscanner.session.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.auth.exception.AuthException;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultSessionService implements SessionService {

	private final SessionRepository sessionRepository;

	@Override
	@Transactional
	public Session save(Session session) {
		return sessionRepository.save(session);
	}

	@Override
	public Session findByAppTokenUuid(String appTokenUuid) {
		return sessionRepository.findByAppTokenUuid(appTokenUuid)
			.orElseThrow(() -> new AuthException("Unissued app token"));
	}

	@Override
	public boolean existsByAppTokenUuidAndRefreshTokenUuid(String appTokenUuid, String refreshTokenUuid) {
		return sessionRepository.existsByAppTokenUuidAndRefreshTokenUuid(appTokenUuid, refreshTokenUuid);
	}

	@Override
	public Session findByMember(Member member) {
		return sessionRepository.findByMember(member)
			.orElseThrow();
	}
}

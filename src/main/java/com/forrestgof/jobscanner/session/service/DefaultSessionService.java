package com.forrestgof.jobscanner.session.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
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
}

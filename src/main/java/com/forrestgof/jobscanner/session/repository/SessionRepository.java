package com.forrestgof.jobscanner.session.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.session.domain.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

	Optional<Session> findByAppTokenUuid(String appTokenUuid);

	boolean existsByAppTokenUuidAndRefreshTokenUuid(String appTokenUuid, String refreshTokenUuid);

}

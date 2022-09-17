package com.forrestgof.jobscanner.session.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.domain.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

	Optional<Session> findByAppTokenUuid(String appTokenUuid);

	boolean existsByAppTokenUuidAndRefreshTokenUuid(String appTokenUuid, String refreshTokenUuid);

	List<Session> findByMember(Member member);
}

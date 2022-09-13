package com.forrestgof.jobscanner.session.service;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.domain.Session;

public interface SessionService {

	Session save(Session session);

	Session findByAppTokenUuid(String appTokenUuid);

	boolean existsByAppTokenUuidAndRefreshTokenUuid(String appTokenUuid, String refreshTokenUuid);

	Session findByMember(Member member);
}

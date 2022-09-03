package com.forrestgof.jobscanner.auth.social;

import com.forrestgof.jobscanner.member.domain.Member;

public interface SocialTokenValidator {

	// String getAccessTokenFromCode(String code);

	Member generateMemberFromAccessToken(String accessToken);
}

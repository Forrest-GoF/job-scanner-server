package com.forrestgof.jobscanner.auth.social;

import com.forrestgof.jobscanner.member.domain.Member;

public interface SocialService {

	String getRedirectUrl();

	Member generateMemberFromCode(String code);
}

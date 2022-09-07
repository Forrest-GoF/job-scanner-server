package com.forrestgof.jobscanner.auth.social;

import com.forrestgof.jobscanner.member.domain.Member;

public interface SocialTokenValidator {

	Member generateMemberFromCode(String code);
}

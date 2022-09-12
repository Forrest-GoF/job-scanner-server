package com.forrestgof.jobscanner.auth.service;

import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.dto.AuthTokenResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

public interface AuthService {

	AuthTokenResponse signIn(Member findMember);

	AuthTokenResponse signIn(SocialMember socialMember);

	void validateMailWithAppToken(String email, String appToken);

	void sendAuthenticationMail(Member member);

	AuthRefreshResponse refreshToken(String appToken, String refreshToken);

	Member getMemberFromAppToken(String appToken);
}

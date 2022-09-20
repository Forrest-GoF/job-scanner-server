package com.forrestgof.jobscanner.auth.service;

import javax.servlet.http.Cookie;

import com.forrestgof.jobscanner.auth.service.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.service.dto.AuthTokenResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

public interface AuthService {

	AuthTokenResponse signIn(Member findMember);

	AuthTokenResponse signIn(SocialMember socialMember);

	void validateMailWithAppToken(String email, String appToken);

	void sendAuthenticationMail(Member member);

	AuthRefreshResponse refreshToken(String appToken, String refreshToken);

	Cookie getCookieFromRefreshToken(String refreshToken);

	Member getMemberFromAppToken(String appToken);

	void deleteSession(Member member);
}

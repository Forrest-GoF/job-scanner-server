package com.forrestgof.jobscanner.auth.service;

import com.forrestgof.jobscanner.auth.dto.AuthLoginResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

public interface AuthService {

	AuthLoginResponse signin(Member findMember);

	AuthLoginResponse signin(SocialMember socialMember);

	AuthRefreshResponse refreshToken(String appToken, String refreshToken);
}

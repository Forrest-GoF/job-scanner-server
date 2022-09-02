package com.forrestgof.jobscanner.auth.service;

import com.forrestgof.jobscanner.auth.dto.AuthLoginResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.member.domain.Member;

public interface AuthService {

	AuthLoginResponse login(Member findMember);

	AuthRefreshResponse refreshToken(String appToken, String refreshToken);
}

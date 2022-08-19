package com.forrestgof.jobscanner.auth.service;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.dto.LoginResponse;

public interface AuthService {

	Member getMemberFromAccessToken(String accessToken);

	void signup(String accessToken);

	LoginResponse login(String accessToken);
}

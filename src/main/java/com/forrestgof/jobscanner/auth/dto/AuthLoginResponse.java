package com.forrestgof.jobscanner.auth.dto;

import com.forrestgof.jobscanner.member.dto.MemberResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthLoginResponse {

	private MemberResponse memberResponse;
	private String appToken;
	private String refreshToken;
}

package com.forrestgof.jobscanner.session.dto;

import com.forrestgof.jobscanner.member.dto.MemberResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

	private MemberResponse memberResponse;
	private String appToken;
	private String refreshToken;
}

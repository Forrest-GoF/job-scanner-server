package com.forrestgof.jobscanner.auth.dto;

import com.forrestgof.jobscanner.member.dto.MemberResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthLoginResponse {

	private MemberResponse memberResponse;
	private String appToken;
	private String refreshToken;

	public static AuthLoginResponse of(
		MemberResponse memberResponse,
		String appToken,
		String refreshToken
	) {
		return new AuthLoginResponse(memberResponse, appToken, refreshToken);
	}
}

package com.forrestgof.jobscanner.auth.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTokenResponse {

	private String appToken;
	private String refreshToken;

	public static AuthTokenResponse of(
		String appToken,
		String refreshToken
	) {
		return new AuthTokenResponse(appToken, refreshToken);
	}
}

package com.forrestgof.jobscanner.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRefreshResponse {

	private String appToken;

	public static AuthRefreshResponse of(String appToken) {
		return new AuthRefreshResponse(appToken);
	}
}

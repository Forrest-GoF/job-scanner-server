package com.forrestgof.jobscanner.auth.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubOAuthResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	public String getTokenTypeAndAccessToken() {
		return tokenType + " " + accessToken;
	}
}

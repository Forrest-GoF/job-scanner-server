package com.forrestgof.jobscanner.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthAccessTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;
}

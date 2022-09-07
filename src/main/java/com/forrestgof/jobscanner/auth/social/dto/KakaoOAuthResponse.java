package com.forrestgof.jobscanner.auth.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoOAuthResponse {

	@JsonProperty("access_token")
	private String accessToken;
}

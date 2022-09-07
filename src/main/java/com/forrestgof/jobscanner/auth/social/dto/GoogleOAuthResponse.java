package com.forrestgof.jobscanner.auth.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleOAuthResponse {

	@JsonProperty("id_token")
	private String idToken;
}

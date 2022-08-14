package com.forrestgof.jobscanner.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRefreshResponse {

	private String appToken;
}

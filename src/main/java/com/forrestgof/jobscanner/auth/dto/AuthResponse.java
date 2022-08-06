package com.forrestgof.jobscanner.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

	private String appToken;
}

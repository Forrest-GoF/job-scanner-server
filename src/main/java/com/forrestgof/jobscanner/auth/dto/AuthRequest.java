package com.forrestgof.jobscanner.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {

	private String accessToken;
}

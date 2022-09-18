package com.forrestgof.jobscanner.auth.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRefreshResponse {

	private String appToken;
}

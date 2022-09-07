package com.forrestgof.jobscanner.auth.social.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserResponse {

	private String id;
	private Properties properties;
	private KakaoAccount kakaoAccount;

	@ToString
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class Properties {
		private String nickname;
		private String profileImage;
	}

	@ToString
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class KakaoAccount {
		private String email;
		private String gender;
	}
}

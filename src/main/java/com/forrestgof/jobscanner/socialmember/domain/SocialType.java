package com.forrestgof.jobscanner.socialmember.domain;

import java.util.Arrays;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

	KAKAO("kakao");
	// GOOGLE("google"),
	// GITHUB("github");

	private final String name;

	public static SocialType getEnum(String inputType) {
		return Arrays.stream(SocialType.values())
			.filter((socialType -> socialType.getName().equals(inputType)))
			.findFirst()
			.orElseThrow(() -> new CustomException(ErrorCode.UNSUPPORTED_SOCIAL_TYPE_EXCEPTION));
	}
}

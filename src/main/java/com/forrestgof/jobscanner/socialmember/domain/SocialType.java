package com.forrestgof.jobscanner.socialmember.domain;

import java.util.Arrays;
import java.util.Locale;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

	KAKAO,
	GOOGLE,
	GITHUB;

	public static SocialType getEnum(String inputType) {
		return Arrays.stream(SocialType.values())
			.filter((socialType -> socialType.name().toLowerCase(Locale.ROOT).equals(inputType)))
			.findFirst()
			.orElseThrow(() -> new CustomException(ErrorCode.UNSUPPORTED_SOCIAL_TYPE_EXCEPTION));
	}
}

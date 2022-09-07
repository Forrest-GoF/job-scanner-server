package com.forrestgof.jobscanner.auth.social;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocialTokenValidatorFactory {

	private final KakaoTokenValidator kakaoTokenValidator;
	// private final GoogleTokenValidator googleTokenValidator;
	// private final GithubTokenValidator githubTokenValidator;

	public SocialTokenValidator find(String socialType) {
		SocialType type = SocialType.getEnum(socialType);
		switch (type) {
			case KAKAO:
				return kakaoTokenValidator;
			// case GOOGLE:
			// 	return googleTokenValidator;
			// case GITHUB:
			// 	return githubTokenValidator;
		}
		return null;
	}
}

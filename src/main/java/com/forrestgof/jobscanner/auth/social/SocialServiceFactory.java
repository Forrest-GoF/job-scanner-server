package com.forrestgof.jobscanner.auth.social;

import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.socialmember.domain.SocialType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocialServiceFactory {

	private final KakaoService kakaoTokenValidator;
	private final GoogleService googleTokenValidator;
	// private final GithubService githubTokenValidator;

	public SocialService find(SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> kakaoTokenValidator;
			case GOOGLE -> googleTokenValidator;
			// case GITHUB -> githubTokenValidator;
		};
	}
}

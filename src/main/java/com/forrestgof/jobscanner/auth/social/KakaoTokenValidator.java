package com.forrestgof.jobscanner.auth.social;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.social.dto.KakaoOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;

import reactor.core.publisher.Mono;

@Component
public class KakaoTokenValidator implements SocialTokenValidator {

	private final String tokenUrl;
	private final String redirectUrl;
	private final String clientId; // JavaScript 키
	private final String clientSecret;

	public KakaoTokenValidator(AuthProperties authProperties) {
		AuthProperties.Client.Kakao kakaoClient = authProperties.client().kakao();
		this.tokenUrl = kakaoClient.tokenUrl();
		this.redirectUrl = kakaoClient.redirectUrl();
		this.clientId = kakaoClient.clientId();
		this.clientSecret = kakaoClient.clientSecret();
	}

	@Override
	public Member generateMemberFromCode(String code) {
		String accessToken = getAccessTokenFromCode(code);
		KakaoUserResponse kakaoUserResponse = getKakaoUserFromAccessToken(accessToken);
		return generateMemberFromKakaoUser(kakaoUserResponse);
	}

	private String getAccessTokenFromCode(String code) {
		return Objects.requireNonNull(WebClient.create()
				.post()
				.uri(tokenUrl)
				.headers(header -> {
					header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
					header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
					header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
				})
				.bodyValue(tokenRequest(code))
				.retrieve()
				.onStatus(
					HttpStatus::is4xxClientError,
					response
						-> Mono.error(new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION)))
				.onStatus(
					HttpStatus::is5xxServerError,
					response
						-> Mono.error(new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION)))
				.bodyToMono(KakaoOAuthResponse.class)
				.block())
			.getAccessToken();
	}

	private MultiValueMap<String, String> tokenRequest(String code) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("grant_type", "authorization_code");
		formData.add("redirect_uri", redirectUrl);
		formData.add("client_id", clientId);
		// formData.add("client_secret", clientSecret);
		return formData;
	}

	private KakaoUserResponse getKakaoUserFromAccessToken(String accessToken) {
		return WebClient.create()
			.get()
			.uri("https://kapi.kakao.com/v2/user/me")
			.headers(h -> h.setBearerAuth(accessToken))
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response
				-> Mono.error(
				new CustomException("Social Access Token is unauthorized", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(HttpStatus::is5xxServerError, response
				-> Mono.error(new CustomException("Internal Server Error", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.bodyToMono(KakaoUserResponse.class)
			.block();
	}

	private Member generateMemberFromKakaoUser(KakaoUserResponse kakaoUserResponse) {
		String email = kakaoUserResponse.getKakaoAccount().getEmail();
		String nickname = kakaoUserResponse.getProperties().getNickname();
		String imageUrl = kakaoUserResponse.getProperties().getProfileImage();
		if (email == null || nickname == null) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION);
		}

		return Member.builder()
			.email(email)
			.nickname(nickname)
			.imageUrl(imageUrl)
			.build();
	}
}

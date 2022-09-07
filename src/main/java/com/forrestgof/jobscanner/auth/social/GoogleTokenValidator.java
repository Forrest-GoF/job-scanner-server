package com.forrestgof.jobscanner.auth.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.social.dto.GoogleOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.GoogleUserResponse;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleTokenValidator implements SocialTokenValidator {

	@Value("${auth.client.google.token-url}")
	private String tokenUrl;
	@Value("${auth.client.google.redirect-url}")
	private String redirectUrl;
	@Value("${auth.client.google.client-id}")
	private String clientId;
	@Value("${auth.client.google.client-secret}")
	private String clientSecret;

	@Override
	public Member generateMemberFromCode(String code) {
		String accessToken = getIdTokenFromCode(code);
		log.warn("accessToken=" + accessToken);
		GoogleUserResponse googleUserResponse = getGoogleUserFromIdToken(accessToken);
		log.warn("googleUserResponse=" + googleUserResponse);
		return generateMemberFromGoogleUser(googleUserResponse);
	}

	private String getIdTokenFromCode(String code) {
		return WebClient.create()
			.post()
			.uri(tokenUrl)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(tokenRequest(code))
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response
				-> Mono.error(
				new CustomException("Social Access Token is unauthorized", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(HttpStatus::is5xxServerError, response
				-> Mono.error(new CustomException("Internal Server Error", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.bodyToMono(GoogleOAuthResponse.class)
			.block()
			.getIdToken();
	}

	private MultiValueMap<String, String> tokenRequest(String code) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("grant_type", "authorization_code");
		formData.add("redirect_uri", redirectUrl);
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		return formData;
	}

	private GoogleUserResponse getGoogleUserFromIdToken(String idToken) {
		return WebClient.create()
			.get()
			.uri("https://oauth2.googleapis.com/tokeninfo",
				builder -> builder.queryParam("id_token", idToken).build())
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response
				-> Mono.error(
				new CustomException("Social Access Token is unauthorized",
					ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(HttpStatus::is5xxServerError, response
				-> Mono.error(new CustomException("Internal Server Error", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.bodyToMono(GoogleUserResponse.class)
			.block();
	}

	private Member generateMemberFromGoogleUser(GoogleUserResponse googleUserResponse) {
		String email = googleUserResponse.getEmail();
		String nickname = googleUserResponse.getName();
		String imageUrl = googleUserResponse.getPicture();
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

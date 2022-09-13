package com.forrestgof.jobscanner.auth.social;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.social.dto.GoogleOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.GoogleUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;

import reactor.core.publisher.Mono;

@Component
public class GoogleTokenValidator implements SocialTokenValidator {

	private final String tokenUrl;
	private final String redirectUrl;
	private final String clientId;
	private final String clientSecret;

	public GoogleTokenValidator(AuthProperties authProperties) {
		AuthProperties.Client.Google googleClient = authProperties.client().google();
		this.tokenUrl = googleClient.tokenUrl();
		this.redirectUrl = googleClient.redirectUrl();
		this.clientId = googleClient.clientId();
		this.clientSecret = googleClient.clientSecret();
	}

	@Override
	public Member generateMemberFromCode(String code) {
		String accessToken = getIdTokenFromCode(code);
		GoogleUserResponse googleUserResponse = getGoogleUserFromIdToken(accessToken);
		return generateMemberFromGoogleUser(googleUserResponse);
	}

	private String getIdTokenFromCode(String code) {
		return Objects.requireNonNull(WebClient.create()
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
				.block())
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
			.onStatus(
				HttpStatus::is4xxClientError,
				response
					-> Mono.error(new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(
				HttpStatus::is5xxServerError,
				response
					-> Mono.error(new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION)))
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
			.isAuthenticatedEmail(true)
			.build();
	}
}

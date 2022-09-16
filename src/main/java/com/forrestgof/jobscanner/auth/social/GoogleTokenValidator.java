package com.forrestgof.jobscanner.auth.social;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.social.dto.GoogleOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.GoogleUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
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
				.onStatus(
					HttpStatus::is4xxClientError,
					response
						-> Mono.error(new AuthCustomException("Failed to get google token with code")))
				.onStatus(
					HttpStatus::is5xxServerError,
					response
						-> Mono.error(new AuthCustomException("Failed to get google token with code")))
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
					-> Mono.error(new AuthCustomException("Failed to get google user information with token")))
			.onStatus(
				HttpStatus::is5xxServerError,
				response
					-> Mono.error(new AuthCustomException("Failed to get google user information with token")))
			.bodyToMono(GoogleUserResponse.class)
			.block();
	}

	private Member generateMemberFromGoogleUser(GoogleUserResponse googleUserResponse) {
		String email = Optional.ofNullable(googleUserResponse.getEmail())
			.orElseThrow(() -> new AuthCustomException("Google email information is missing"));

		String nickname = Optional.ofNullable(googleUserResponse.getName())
			.orElseThrow(() -> new AuthCustomException("Google nickname information is missing"));

		String imageUrl = googleUserResponse.getPicture();

		return Member.builder()
			.email(email)
			.nickname(nickname)
			.imageUrl(imageUrl)
			.isAuthenticatedEmail(true)
			.build();
	}
}

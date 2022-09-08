package com.forrestgof.jobscanner.auth.social;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.social.dto.GithubOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.GithubUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;

import reactor.core.publisher.Mono;

@Component
public class GithubTokenValidator implements SocialTokenValidator {

	private final String tokenUrl;
	private final String redirectUrl;
	private final String clientId;
	private final String clientSecret;

	public GithubTokenValidator(AuthProperties authProperties) {
		AuthProperties.Client.Github githubClient = authProperties.client().github();
		this.tokenUrl = githubClient.tokenUrl();
		this.redirectUrl = githubClient.redirectUrl();
		this.clientId = githubClient.clientId();
		this.clientSecret = githubClient.clientSecret();
	}

	@Override
	public Member generateMemberFromCode(String code) {
		String accessToken = getAccessTokenFromCode(code);
		GithubUserResponse githubUserResponse = getGithubUserFromAccessToken(accessToken);
		return generateMemberFromGithubUser(githubUserResponse);
	}

	private String getAccessTokenFromCode(String code) {
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
				.bodyToMono(GithubOAuthResponse.class)
				.block())
			.getTokenTypeAndAccessToken();
	}

	private MultiValueMap<String, String> tokenRequest(String code) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		return formData;
	}

	private GithubUserResponse getGithubUserFromAccessToken(String accessToken) {
		return WebClient.create()
			.get()
			.uri("https://api.github.com/user")
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, accessToken)
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError, response
				-> Mono.error(
				new CustomException("Social Access Token is unauthorized", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.onStatus(HttpStatus::is5xxServerError, response
				-> Mono.error(new CustomException("Internal Server Error", ErrorCode.INVALID_TOKEN_EXCEPTION)))
			.bodyToMono(GithubUserResponse.class)
			.block();
	}

	private Member generateMemberFromGithubUser(GithubUserResponse githubUserResponse) {
		String email = githubUserResponse.getEmail(); //null 일 수 있음
		String nickname = githubUserResponse.getLogin();
		String imageUrl = githubUserResponse.getAvatarUrl();
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

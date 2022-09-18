package com.forrestgof.jobscanner.auth.social;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.social.dto.GithubOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.GithubUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
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
	public String getRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", clientId);
		params.put("redirect_uri", redirectUrl);

		String parameterString = params.entrySet()
			.stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));

		return "https://github.com/login/oauth/authorize" + "?" + parameterString;
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
				.onStatus(
					HttpStatus::is4xxClientError,
					response
						-> Mono.error(new AuthCustomException("Failed to get github token with code")))
				.onStatus(
					HttpStatus::is5xxServerError,
					response
						-> Mono.error(new AuthCustomException("Failed to get github token with code")))
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
			.onStatus(
				HttpStatus::is4xxClientError,
				response
					-> Mono.error(new AuthCustomException("Failed to get github user information with token")))
			.onStatus(
				HttpStatus::is5xxServerError,
				response
					-> Mono.error(new AuthCustomException("Failed to get github user information with token")))
			.bodyToMono(GithubUserResponse.class)
			.block();
	}

	private Member generateMemberFromGithubUser(GithubUserResponse githubUserResponse) {
		String email = Optional.ofNullable(githubUserResponse.getEmail())
			.orElseThrow(() -> new AuthCustomException("Github email information is missing")); //null 일 수 있음

		String nickname = Optional.ofNullable(githubUserResponse.getLogin())
			.orElseThrow(() -> new AuthCustomException("Github nickname information is missing"));

		String imageUrl = githubUserResponse.getAvatarUrl();

		return Member.builder()
			.email(email)
			.nickname(nickname)
			.imageUrl(imageUrl)
			.isAuthenticatedEmail(true)
			.build();
	}
}

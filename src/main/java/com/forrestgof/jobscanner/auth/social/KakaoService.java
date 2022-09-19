package com.forrestgof.jobscanner.auth.social;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.social.dto.KakaoOAuthResponse;
import com.forrestgof.jobscanner.auth.social.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
import com.forrestgof.jobscanner.member.domain.Member;

import reactor.core.publisher.Mono;

@Service
public class KakaoService implements SocialService {

	private final String tokenUrl;
	private final String redirectUrl;
	private final String clientId; // JavaScript 키
	private final String clientSecret;

	public KakaoService(AuthProperties authProperties) {
		AuthProperties.Client.Kakao kakaoClient = authProperties.client().kakao();
		this.tokenUrl = kakaoClient.tokenUrl();
		this.redirectUrl = kakaoClient.redirectUrl();
		this.clientId = kakaoClient.clientId();
		this.clientSecret = kakaoClient.clientSecret();
	}

	@Override
	public String getRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", clientId);
		params.put("redirect_uri", redirectUrl);
		params.put("response_type", "code");

		String parameterString = params.entrySet()
			.stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));

		return "https://kauth.kakao.com/oauth/authorize" + "?" + parameterString;
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
						-> Mono.error(new AuthCustomException("Failed to get kakao token with code")))
				.onStatus(
					HttpStatus::is5xxServerError,
					response
						-> Mono.error(new AuthCustomException("Failed to get kakao token with code")))
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
			.onStatus(
				HttpStatus::is4xxClientError,
				response
					-> Mono.error(new AuthCustomException("Failed to get kakao user information with token")))
			.onStatus(
				HttpStatus::is5xxServerError,
				response
					-> Mono.error(new AuthCustomException("Failed to get kakao user information with token")))
			.bodyToMono(KakaoUserResponse.class)
			.block();
	}

	private Member generateMemberFromKakaoUser(KakaoUserResponse kakaoUserResponse) {
		String email = Optional.ofNullable(kakaoUserResponse.getKakaoAccount().getEmail())
			.orElseThrow(() -> new AuthCustomException("Kakao email information is missing"));

		String nickname = Optional.ofNullable(kakaoUserResponse.getProperties().getNickname())
			.orElseThrow(() -> new AuthCustomException("Kakao nickname information is missing"));

		String imageUrl = kakaoUserResponse.getProperties().getProfileImage();

		return Member.builder()
			.email(email)
			.nickname(nickname)
			.imageUrl(imageUrl)
			.isAuthenticatedEmail(true)
			.build();
	}
}

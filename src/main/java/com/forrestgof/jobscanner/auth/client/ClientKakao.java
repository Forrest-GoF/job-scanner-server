package com.forrestgof.jobscanner.auth.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.forrestgof.jobscanner.auth.dto.KakaoUserResponse;
import com.forrestgof.jobscanner.auth.exception.TokenValidFailedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientKakao {

	private final WebClient webClient;

	public KakaoUserResponse getUserData(String accessToken) {
		try {
			return webClient.get()
				.uri("https://kapi.kakao.com/v2/user/me")
				.headers(h -> h.setBearerAuth(accessToken))
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response
					-> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
				.onStatus(HttpStatus::is5xxServerError, response
					-> Mono.error(new TokenValidFailedException("Internal Server Error")))
				.bodyToMono(KakaoUserResponse.class)
				.block();
		} catch (TokenValidFailedException e) {
			return null;
		}
	}
}

package com.forrestgof.jobscanner.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "auth")
public record AuthProperties(
	Client client,
	Jwt jwt
) {

	public record Client(
		Kakao kakao,
		Google google,
		Github github
	) {
		public record Kakao(
			String tokenUrl,
			String redirectUrl,
			String clientId,
			String clientSecret
		) {
		}

		public record Google(
			String tokenUrl,
			String redirectUrl,
			String clientId,
			String clientSecret
		) {
		}

		public record Github(
			String tokenUrl,
			String redirectUrl,
			String clientId,
			String clientSecret
		) {
		}
	}

	public record Jwt(
		String appTokenSecret,
		String refreshTokenSecret,
		String appTokenExpiry,
		String refreshTokenExpiry
	) {
	}
}

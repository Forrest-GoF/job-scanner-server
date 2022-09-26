package com.forrestgof.jobscanner.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "aws")
public record AwsProperties(
	Cloudwatch cloudwatch,
	Loadbalancer loadbalancer
) {

	public record Cloudwatch(
		Credentials credentials
	) {

		public record Credentials(
			String accessKey,
			String secretKey
		) {
		}
	}

	public record Loadbalancer(
		String healthCheckPath
	) {
	}
}

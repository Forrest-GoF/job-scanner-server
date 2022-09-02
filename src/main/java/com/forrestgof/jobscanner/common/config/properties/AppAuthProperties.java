package com.forrestgof.jobscanner.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "app.auth")
public record AppAuthProperties(
	String appTokenSecret,
	String refreshTokenSecret,
	String appTokenExpiry,
	String refreshTokenExpiry
) {

}

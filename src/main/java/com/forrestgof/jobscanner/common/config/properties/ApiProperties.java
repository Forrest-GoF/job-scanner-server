package com.forrestgof.jobscanner.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("api")
public record ApiProperties(
	String crawlerEndpoint,
	String npsKey
) {

}

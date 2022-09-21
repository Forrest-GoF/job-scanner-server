package com.forrestgof.jobscanner.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.forrestgof.jobscanner.common.config.properties.ApiProperties;
import com.forrestgof.jobscanner.common.config.properties.AuthProperties;
import com.forrestgof.jobscanner.common.config.properties.AwsProperties;
import com.forrestgof.jobscanner.common.config.properties.DomainProperties;

@Configuration
@EnableConfigurationProperties(value = {
	ApiProperties.class,
	AuthProperties.class,
	DomainProperties.class,
	AwsProperties.class
})
public class PropertiesConfig {
}

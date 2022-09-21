package com.forrestgof.jobscanner.common.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.forrestgof.jobscanner.common.config.properties.AwsProperties;

@Configuration
public class AwsConfig {
	private final Logger log = LoggerFactory.getLogger(AwsConfig.class);

	private final String accessKey;
	private final String secretKey;

	public AwsConfig(AwsProperties awsProperties) {
		AwsProperties.Cloudwatch.Credentials credentials = awsProperties.cloudwatch().credentials();
		this.accessKey = credentials.AccessKey();
		this.secretKey = credentials.SecretKey();
	}

	private AWSCredentials awsCredentials;

	@PostConstruct
	public void init() {
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	}

	@Bean
	public AWSCredentialsProvider awsCredentialsProvider() {
		return new AWSStaticCredentialsProvider(awsCredentials);
	}
}

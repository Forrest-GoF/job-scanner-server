package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.forrestgof.jobscanner.common.config.properties.ApiProperties;
import com.forrestgof.jobscanner.jobposting.util.dto.PlatformJobDto;

@Component
public class PlatformJobCrawler {

	private final String crawlerEndpoint;
	WebClient webClient = WebClient.create();

	public PlatformJobCrawler(ApiProperties apiProperties) {
		crawlerEndpoint = apiProperties.crawlerEndpoint();
	}

	public PlatformJobDto callCrawler(Platform platform, String url) {
		URI uri = UriComponentsBuilder.fromHttpUrl(crawlerEndpoint + platform.getUrl())
			.queryParam("url", url)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUri();

		return webClient.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(PlatformJobDto.class)
			.block();
	}
}

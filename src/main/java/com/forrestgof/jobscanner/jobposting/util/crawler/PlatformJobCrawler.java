package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.forrestgof.jobscanner.jobposting.util.dto.PlatformJobDto;

@Component
public class PlatformJobCrawler {

	@Value("${api.crawler-endpoint}")
	private String crawlerEndpoint;

	WebClient webClient = WebClient.create();

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

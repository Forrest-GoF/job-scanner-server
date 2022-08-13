package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.forrestgof.jobscanner.jobposting.util.dto.GoogleCrawlingResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleJobCrawler {

	@Value("${api.crawler-endpoint}")
	private String crawlerEndpoint;

	WebClient webClient = WebClient.create();

	public GoogleCrawlingResponse callCrawler(String searchQuery, int offset) {
		URI uri = UriComponentsBuilder.fromHttpUrl(crawlerEndpoint + "/search")
			.queryParam("q", searchQuery)
			.queryParam("start", offset)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUri();

		return webClient.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(GoogleCrawlingResponse.class)
			.block();
	}
}

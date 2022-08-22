package com.forrestgof.jobscanner.company.util.nps;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.forrestgof.jobscanner.company.util.dto.NpsBassDto;
import com.forrestgof.jobscanner.company.util.dto.NpsBassResponse;
import com.forrestgof.jobscanner.company.util.dto.NpsDetailDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NpsApiExplorer {

	@Value("${api.crawler-endpoint}")
	private String crawlerEndpoint;

	private final WebClient webClient = WebClient.create();

	public NpsBassResponse getBass(String companyName) {

		URI uri = UriComponentsBuilder.fromHttpUrl(crawlerEndpoint + "/nps")
			.queryParam("q", companyName)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUri();

		return webClient.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(NpsBassResponse.class)
			.block();
	}

	public NpsDetailDto getDetail(NpsBassDto npsBassDto) {

		URI uri = UriComponentsBuilder.fromHttpUrl(crawlerEndpoint + "/nps/" + npsBassDto.getSeq())
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUri();

		return webClient.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(NpsDetailDto.class)
			.block();
	}
}

package com.forrestgof.jobscanner.jobposting.util.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleCrawlingResponse {
	String crawlingUrl;
	List<GoogleJobDto> data;
	int length;
}

package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.jobposting.util.dto.GoogleCrawlingResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchUtils {

	private final GoogleJobCrawler googleJobCrawler;
	private final CrawlingDataParser crawlingDataParser;

	public void batchCrawling(List<String> searchKeywords) {
		searchKeywords.forEach(this::search);
	}

	private void search(String searchKeyword) {
		for (int offset=0; true; offset+=10) {
			GoogleCrawlingResponse googleCrawlingResponse = googleJobCrawler.callCrawler(searchKeyword, offset);
			if (googleCrawlingResponse.getLength() == 0) {
				break;
			}
			googleCrawlingResponse.getData().forEach(crawlingDataParser::parse);
		}
	}
}

package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.jobposting.service.JobPostingService;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleCrawlingResponse;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleJobDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchUtils {

	private final GoogleJobCrawler googleJobCrawler;
	private final CrawlingDataParser crawlingDataParser;
	private final JobPostingService jobPostingService;

	public void batchCrawling(List<String> searchKeywords) {
		for (String searchKeyword : searchKeywords) {
			int offset = 0;
			while(true) {
				GoogleCrawlingResponse googleCrawlingResponse = googleJobCrawler.callCrawler(searchKeyword, offset);
				if (googleCrawlingResponse.getLength() == 0)
					break;

				filterNewJob(googleCrawlingResponse)
					.forEach(crawlingDataParser::saveGoogleJob);
				offset += 10;
			}
		}
	}

	private List<GoogleJobDto> filterNewJob(GoogleCrawlingResponse googleCrawlingResponse) {
		List<GoogleJobDto> newJobs = new ArrayList<>();
		googleCrawlingResponse.getData().stream()
			.filter(Predicate.not(job -> jobPostingService.existsByGoogleKey(job.key())))
			.forEach(newJobs::add);
		return newJobs;
	}
}

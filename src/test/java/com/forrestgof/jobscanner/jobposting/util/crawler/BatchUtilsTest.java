package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class BatchUtilsTest {

	@Autowired
	BatchUtils batchUtils;
	@Autowired JobPostingService jobPostingService;

	@Test
	public void batchCrawlingTest() throws Exception {
	    //given
		List<String> searchKeywords = new ArrayList<>();
		searchKeywords.add("개발");

		//when
		batchUtils.batchCrawling(searchKeywords);

	    //then
		List<JobPosting> findAll = jobPostingService.findAll();
		for (JobPosting jobPosting : findAll) {
			System.out.println("jobPosting = " + jobPosting.getTitle());
		}

		Assertions.assertThat(findAll.size()).isGreaterThan(0);
	}
}

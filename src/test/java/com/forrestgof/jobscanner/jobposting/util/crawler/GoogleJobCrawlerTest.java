package com.forrestgof.jobscanner.jobposting.util.crawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.forrestgof.jobscanner.jobposting.util.dto.GoogleCrawlingResponse;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class GoogleJobCrawlerTest {

	@Autowired GoogleJobCrawler googleJobCrawler;

	@Test
	public void googleCrawler() throws Exception {
	    //given
		String searchQuery = "개발";
		int offset = 0;

		//when
 		GoogleCrawlingResponse crawlingResponse = googleJobCrawler.callCrawler(searchQuery, offset);

	    //then
		Assertions.assertThat(crawlingResponse.getLength()).isEqualTo(crawlingResponse.getData().size());
	}
}

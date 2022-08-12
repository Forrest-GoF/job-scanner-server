package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.forrestgof.jobscanner.company.repository.CompanyRepository;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.repository.TagRepository;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleJobDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CrawlingDataParserTest {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	TagRepository tagRepository;
	@Autowired
	PlatformJobCrawler platformJobCrawler;
	@Autowired
	CrawlingDataParser crawlingDataParser;

	@Test
	public void wantedTest() throws Exception {
		//given
		GoogleJobDto googleJobDto = new GoogleJobDto();
		googleJobDto.setKey("123");
		googleJobDto.setCompanyName("테스트");
		googleJobDto.setApplyUrl(
			"https://www.wanted.co.kr/wd/92545");
		googleJobDto.setPlatform("원티드");
		googleJobDto.setType("정규직");

		//when
		JobPosting jobPosting = crawlingDataParser.saveGoogleJob(googleJobDto);

		//then
		Assertions.assertThat(jobPosting.getJobDetail().getMainTask()).isNotEmpty();
	}

	@Test
	public void wantedTagTest() throws Exception {
		//given
		GoogleJobDto googleJobDto = new GoogleJobDto();
		googleJobDto.setKey("123");
		googleJobDto.setCompanyName("테스트");
		googleJobDto.setApplyUrl(
			"https://www.wanted.co.kr/wd/69913");
		googleJobDto.setPlatform("원티드");
		googleJobDto.setType("정규직");

		//when
		JobPosting jobPosting = crawlingDataParser.saveGoogleJob(googleJobDto);

		//then
		Assertions.assertThat(jobPosting.getJobTags().size()).isGreaterThan(0);
	}

	@Test
	public void setPostedAtTest() throws Exception {
		//given
		GoogleJobDto googleJobDto = new GoogleJobDto();
		googleJobDto.setKey("1234");
		googleJobDto.setType("정규직");
		googleJobDto.setPostedAt("3일 전");
		googleJobDto.setCompanyName("테스트");

		//when
		JobPosting jobPosting = crawlingDataParser.saveGoogleJob(googleJobDto);

		//then
		Assertions.assertThat(jobPosting.getPostedAt()).isEqualTo(LocalDate.now().minusDays(3L));
	}
}

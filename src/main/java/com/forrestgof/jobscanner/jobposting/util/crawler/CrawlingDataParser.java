package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.service.CompanyService;
import com.forrestgof.jobscanner.jobposting.domain.JobDetail;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobTag;
import com.forrestgof.jobscanner.jobposting.domain.JobType;
import com.forrestgof.jobscanner.jobposting.domain.Tag;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;
import com.forrestgof.jobscanner.jobposting.service.JobTagService;
import com.forrestgof.jobscanner.jobposting.service.TagService;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleJobDto;
import com.forrestgof.jobscanner.jobposting.util.dto.PlatformJobDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CrawlingDataParser {

	private final JobPostingService jobPostingService;
	private final CompanyService companyService;
	private final TagService tagService;
	private final JobTagService jobTagService;
	private final PlatformJobCrawler platformJobCrawler;

	public void parse(GoogleJobDto googleJobDto) {
		if(isNotNewJob(googleJobDto)) {
			return;
		}

		Company savedCompany = getCompany(googleJobDto);
		JobPosting jobPosting = parseJobPosting(googleJobDto, savedCompany);
		List<String> stacks = new ArrayList<>();

		try {
			PlatformJobDto platformJobDto = Platform.of(jobPosting.getPlatform())
				.map(platform -> platformJobCrawler.callCrawler(platform, googleJobDto.applyUrl()))
				.orElseThrow();

			parsePlatformJob(jobPosting, platformJobDto);
			stacks = platformJobDto.stacks();
		} catch (WebClientResponseException e) {
			jobPosting.expire();
		} catch (NoSuchElementException ignored) { }

		JobPosting savedJobPosting = jobPostingService.save(jobPosting);
		parseJobTag(savedJobPosting, stacks);
	}

	private boolean isNotNewJob(GoogleJobDto googleJobDto) {
		String googleKey = googleJobDto.key();
		return jobPostingService.existsByGoogleKey(googleKey);
	}

	private Company getCompany(GoogleJobDto googleJobDto) {
		String companyName = googleJobDto.companyName();
		String thumbnail = googleJobDto.thumbnail();

		return companyService.findByRawName(companyName)
			.orElseGet(() -> companyService.createFrom(companyName, thumbnail));
	}

	private JobPosting parseJobPosting(GoogleJobDto googleJobDto, Company company) {
		LocalDate postedAt = parsePostedAt(googleJobDto.postedAt());
		JobType jobType = JobType.from(googleJobDto.type());

		return JobPosting.builder()
			.applyUrl(googleJobDto.applyUrl())
			.company(company)
			.summary(googleJobDto.description())
			.googleKey(googleJobDto.key())
			.location(googleJobDto.location())
			.platform(googleJobDto.platform())
			.salary(googleJobDto.salary())
			.postedAt(postedAt)
			.title(googleJobDto.title())
			.type(jobType)
			.build();
	}

	private void parsePlatformJob(JobPosting jobPosting, PlatformJobDto platformJobDto) {
		String location = platformJobDto.location();
		LocalDate expiredAt = parseExpiredAt(platformJobDto.deadline());

		JobDetail jobDetail = JobDetail.builder()
			.benefit(platformJobDto.benefit())
			.introduction(platformJobDto.introduction())
			.mainTask(platformJobDto.mainTask())
			.preferential(platformJobDto.preferential())
			.qualification(platformJobDto.qualification())
			.build();

		jobPosting.updateFromPlatform(location, expiredAt, jobDetail);
	}

	private void parseJobTag(JobPosting saveJobPosting, List<String> stacks) {
		stacks.stream()
			.map(this::parseTag)
			.map(tag -> new JobTag(saveJobPosting, tag))
			.map(jobTagService::save)
			.forEach(saveJobPosting::addJobTag);
	}

	private Tag parseTag(String name) {
		if (tagService.existsByName(name)) {
			return tagService.findByName(name);
		}
		return tagService.save(new Tag(name));
	}

	private LocalDate parsePostedAt(String postedAt) {
		if (postedAt==null) {
			return null;
		}

		//'전' 단어 분리
		String s = postedAt.split(" ")[0];

		//숫자 & 문자 분리
		String num = s.replaceAll("[^0-9]", "");
		String str = s.replaceAll("[0-9]", "");

		LocalDateTime now = LocalDateTime.now();
		return switch (str) {
			case "시간" -> now.minusHours(Long.parseLong(num)).toLocalDate();
			case "일" -> now.minusDays(Long.parseLong(num)).toLocalDate();
			case "개월" -> now.minusMonths(Long.parseLong(num)).toLocalDate();
			default -> null;
		};
	}

	private LocalDate parseExpiredAt(String expiredAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			return LocalDate.parse(expiredAt, formatter);
		} catch (Exception e) {
			return null;
		}
	}

}

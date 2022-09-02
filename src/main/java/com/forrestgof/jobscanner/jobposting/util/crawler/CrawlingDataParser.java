package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

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

	public JobPosting saveGoogleJob(GoogleJobDto googleJobDto) {
		String companyName = googleJobDto.companyName();
		Long companyId;
		if (!companyService.existsByGoogleName(companyName)) {
			companyId = companyService.createFromGoogleJob(googleJobDto);
		} else {
			companyId = companyService.findByGoogleName(companyName).getId();
		}

		Company company = companyService.findOne(companyId);
		LocalDate postedAt = parsePostedAt(googleJobDto.postedAt());
		JobType jobType = JobType.of(googleJobDto.type());

		JobPosting jobPosting = JobPosting.builder()
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

		Optional<Platform> optionalPlatform = Platform.of(googleJobDto.platform());

		if (optionalPlatform.isEmpty()) {
			return jobPostingService.save(jobPosting);
		}

		Platform platform = optionalPlatform.get();
		PlatformJobDto platformJobDto = platformJobCrawler.callCrawler(platform, googleJobDto.applyUrl());
		return parsePlatformJob(jobPosting, platformJobDto);
	}

	private JobPosting parsePlatformJob(JobPosting jobPosting, PlatformJobDto platformJobDto) {
		LocalDate expiredAt = parseExpiredAt(platformJobDto.deadline());
		List<String> stacks = platformJobDto.stacks();

		JobDetail jobDetail = JobDetail.builder()
			.benefit(platformJobDto.benefit())
			.introduction(platformJobDto.introduction())
			.mainTask(platformJobDto.mainTask())
			.preferential(platformJobDto.preferential())
			.qualification(platformJobDto.qualification())
			.build();

		jobPosting.setLocation(platformJobDto.location());
		jobPosting.setExpiredAt(expiredAt);
		jobPosting.setJobDetail(jobDetail);

		JobPosting saveJobPosting = jobPostingService.save(jobPosting);

		stacks.stream()
			.map(this::parseTag)
			.map(tag -> new JobTag(saveJobPosting, tag))
			.map(jobTagService::save)
			.forEach(saveJobPosting::addJobTag);

		return saveJobPosting;
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

package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.service.CompanyService;
import com.forrestgof.jobscanner.jobposting.domain.JobDetail;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting.JobPostingBuilder;
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

		Company company = parseCompany(googleJobDto.getCompanyName(), googleJobDto.getThumbnail());
		LocalDate postedAt = parsePostedAt(googleJobDto.getPostedAt());
		JobType jobType = JobType.of(googleJobDto.getType());

		JobPostingBuilder jobPostingBuilder = JobPosting.builder()
			.applyUrl(googleJobDto.getApplyUrl())
			.company(company)
			.summary(googleJobDto.getDescription())
			.googleKey(googleJobDto.getKey())
			.location(googleJobDto.getLocation())
			.platform(googleJobDto.getPlatform())
			.salary(googleJobDto.getSalary())
			.postedAt(postedAt)
			.title(googleJobDto.getTitle())
			.type(jobType);

		Optional<Platform> optionalPlatform = Platform.of(googleJobDto.getPlatform());

		if (optionalPlatform.isEmpty()) {
			return jobPostingService.save(jobPostingBuilder.build());
		}

		Platform platform = optionalPlatform.get();
		PlatformJobDto platformJobDto = platformJobCrawler.callCrawler(platform, googleJobDto.getApplyUrl());
		return parsePlatformJob(jobPostingBuilder, platformJobDto);
	}

	private JobPosting parsePlatformJob(JobPostingBuilder jobPostingBuilder, PlatformJobDto platformJobDto) {
		LocalDate expiredAt = parseExpiredAt(platformJobDto.getDeadline());
		List<String> stacks = platformJobDto.getStacks();

		JobDetail jobDetail = JobDetail.builder()
			.benefit(platformJobDto.getBenefit())
			.introduction(platformJobDto.getIntroduction())
			.mainTask(platformJobDto.getMainTask())
			.preferential(platformJobDto.getPreferential())
			.qualification(platformJobDto.getQualification())
			.build();

		JobPosting jobPosting = jobPostingBuilder
			.location(platformJobDto.getLocation())
			.expiredAt(expiredAt)
			.jobDetail(jobDetail)
			.build();

		JobPosting saveJobPosting = jobPostingService.save(jobPosting);

		stacks.stream()
			.map(this::parseTag)
			.map(tag -> new JobTag(saveJobPosting, tag))
			.map(jobTagService::save)
			.forEach(saveJobPosting::addJobTag);

		return saveJobPosting;
	}

	private Company parseCompany(String companyName, String thumbnail) {
		companyName = companyName.replace(" ", "");	//공백 제거

		try {
			return companyService.findByName(companyName);
		} catch (Exception e) {
			Company company = Company.builder()
				.name(companyName)
				.thumbnailUrl(thumbnail)
				.build();

			return companyService.save(company);
		}
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
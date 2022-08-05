package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobStack;
import com.forrestgof.jobscanner.jobposting.domain.TechStack;

import lombok.Data;

@Data
public class JobPreviewDto {
	Long id;
	String title;
	String companyName;
	String companyThumbnail;
	int salary;
	String platform;
	LocalDateTime expiredAt;
	List<String> stacks;

	public JobPreviewDto(JobPosting jobPosting) {
		id = jobPosting.getId();
		title = jobPosting.getTitle();
		companyName = jobPosting.getCompany().getName();
		companyThumbnail = jobPosting.getCompany().getThumbnailUrl();
		salary = jobPosting.getSalary();
		platform = jobPosting.getPlatform();
		expiredAt = jobPosting.getExpiredAt();
		stacks = jobPosting.getJobStacks().stream()
			.map(JobStack::getTechStack)
			.map(TechStack::getName)
			.collect(Collectors.toList());
	}
}

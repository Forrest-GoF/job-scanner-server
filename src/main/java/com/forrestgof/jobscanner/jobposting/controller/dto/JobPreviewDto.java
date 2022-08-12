package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobTag;
import com.forrestgof.jobscanner.jobposting.domain.Tag;

import lombok.Data;

@Data
public class JobPreviewDto {
	Long id;
	String title;
	String companyName;
	String companyThumbnail;
	String salary;
	String platform;
	String expiredAt;
	List<String> stacks;

	public JobPreviewDto(JobPosting jobPosting) {
		id = jobPosting.getId();
		title = jobPosting.getTitle();
		companyName = jobPosting.getCompany().getName();
		companyThumbnail = jobPosting.getCompany().getThumbnailUrl();
		salary = jobPosting.getSalary();
		platform = jobPosting.getPlatform();
		if (jobPosting.getExpiredAt() != null)
			expiredAt = jobPosting.getExpiredAt().toString();
		stacks = jobPosting.getJobTags().stream()
			.map(JobTag::getTag)
			.map(Tag::getName)
			.collect(Collectors.toList());
	}
}

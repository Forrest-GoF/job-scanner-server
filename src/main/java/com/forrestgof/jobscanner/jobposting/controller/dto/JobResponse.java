package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobTag;
import com.forrestgof.jobscanner.tag.domain.Tag;

import lombok.Data;

@Data
public class JobResponse {
	Long id;
	String title;
	CompanyDto company;
	String location;
	String postedAt;
	String expiredAt;
	String platform;
	String applyUrl;
	String type;
	String salary;
	String education;
	String career;
	String summary;
	List<String> tags;
	JobDetailDto jobDetail;

	public JobResponse(JobPosting jobPosting) {
		id = jobPosting.getId();
		title = jobPosting.getTitle();
		company = new CompanyDto(jobPosting.getCompany());
		location = jobPosting.getLocation();
		if (jobPosting.getPostedAt() != null)
			postedAt = jobPosting.getPostedAt().toString();
		if (jobPosting.getExpiredAt() != null)
			expiredAt = jobPosting.getExpiredAt().toString();
		platform = jobPosting.getPlatform();
		applyUrl = jobPosting.getApplyUrl();
		type = jobPosting.getType().getName();
		salary = jobPosting.getSalary();
		education = jobPosting.getEducation();
		career = jobPosting.getCareer();
		summary = jobPosting.getSummary();
		tags = jobPosting.getJobTags().stream()
			.map(JobTag::getTag)
			.map(Tag::getName)
			.collect(Collectors.toList());
		if (jobPosting.getJobDetail() != null)
			jobDetail = new JobDetailDto(jobPosting.getJobDetail());
	}
}

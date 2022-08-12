package com.forrestgof.jobscanner.jobposting.controller.dto;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;

import lombok.Data;

@Data
public class JobDto {
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
	JobDetailDto jobDetail;

	public JobDto(JobPosting jobPosting) {
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
		jobDetail = new JobDetailDto(jobPosting.getJobDetail());
	}
}

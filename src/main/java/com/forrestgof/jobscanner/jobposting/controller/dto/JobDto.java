package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.time.format.DateTimeFormatter;

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
	int salary;
	String education;
	String career;
	JobDetailDto jobDetail;

	public JobDto(JobPosting jobPosting) {
		id = jobPosting.getId();
		title = jobPosting.getTitle();
		company = new CompanyDto(jobPosting.getCompany());
		location = jobPosting.getLocation();
		postedAt = jobPosting.getPostedAt().format(DateTimeFormatter.ISO_LOCAL_DATE);;
		expiredAt = jobPosting.getExpiredAt().format(DateTimeFormatter.ISO_LOCAL_DATE);;
		platform = jobPosting.getPlatform();
		applyUrl = jobPosting.getApplyUrl();
		type = jobPosting.getType();
		salary = jobPosting.getSalary();
		education = jobPosting.getEducation();
		career = jobPosting.getCareer();
		jobDetail = new JobDetailDto(jobPosting.getJobDetail());
	}
}

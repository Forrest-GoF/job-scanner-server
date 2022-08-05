package com.forrestgof.jobscanner.jobposting.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "JOB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPosting {

	@Id
	@GeneratedValue
	@Column(name = "job_id")
	private Long id;

	private String googleKey;

	@Column(name = "job_title")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "job_location")
	private String location;

	private LocalDateTime postedAt;
	private LocalDateTime expiredAt;
	private String platform;
	private String applyUrl;
	private String type;
	private int salary;
	private String education;
	private String career;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_detail_id")
	private JobDetail jobDetail;

	@OneToMany(mappedBy = "jobPosting")
	private List<JobStack> jobStacks = new ArrayList<>();

	@Builder
	public JobPosting(String googleKey, String title, Company company, String location, LocalDateTime postedAt,
		LocalDateTime expiredAt, String platform, String applyUrl, String type, int salary, String education,
		String career, JobDetail jobDetail) {
		this.googleKey = googleKey;
		this.title = title;
		this.company = company;
		this.location = location;
		this.postedAt = postedAt;
		this.expiredAt = expiredAt;
		this.platform = platform;
		this.applyUrl = applyUrl;
		this.type = type;
		this.salary = salary;
		this.education = education;
		this.career = career;
		this.jobDetail = jobDetail;
	}
}

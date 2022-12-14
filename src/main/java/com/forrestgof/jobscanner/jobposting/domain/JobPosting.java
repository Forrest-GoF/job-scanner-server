package com.forrestgof.jobscanner.jobposting.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.company.domain.Company;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "JOB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPosting extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "job_id")
	private Long id;

	@Column(unique = true)
	private String googleKey;

	@Column(name = "job_title")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "job_location")
	private String location;

	private LocalDate postedAt;
	private LocalDate expiredAt;
	private boolean isExpired;
	private String platform;

	@Column(columnDefinition = "TEXT")
	private String applyUrl;

	@Enumerated(value = EnumType.STRING)
	private JobType type;

	private String salary;
	private String education;
	private String career;

	@Column(columnDefinition = "TEXT")
	private String summary;

	@Embedded
	private JobDetail jobDetail;

	@OneToMany(mappedBy = "jobPosting")
	private final List<JobTag> jobTags = new ArrayList<>();

	private int views;
	private int viewsPerWeek;

	@Builder
	public JobPosting(
		String googleKey,
		String title,
		Company company,
		String location,
		LocalDate postedAt,
		LocalDate expiredAt,
		boolean isExpired,
		String platform,
		String applyUrl,
		JobType type,
		String salary,
		String education,
		String career,
		String summary,
		JobDetail jobDetail
	) {
		this.googleKey = googleKey;
		this.title = title;
		this.company = company;
		this.location = location;
		this.postedAt = postedAt;
		this.isExpired = isExpired;
		this.expiredAt = expiredAt;
		this.platform = platform;
		this.applyUrl = applyUrl;
		this.type = type;
		this.salary = salary;
		this.education = education;
		this.career = career;
		this.summary = summary;
		this.jobDetail = jobDetail;
	}

	public void addJobTag(JobTag jobTag) {
		jobTags.add(jobTag);
	}

	public void updateFromPlatform(
		String location,
		LocalDate expiredAt,
		JobDetail jobDetail
	) {
		this.expiredAt = expiredAt;
		this.jobDetail = jobDetail;
		this.location = location;
	}

	public void expire() {
		isExpired = true;
	}

	public void increaseViews() {
		views++;
		viewsPerWeek++;
	}
}

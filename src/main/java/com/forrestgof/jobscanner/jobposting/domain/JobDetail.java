package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobDetail {

	@Id
	@GeneratedValue
	@Column(name = "job_detail_id")
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String summary;

	@Column(columnDefinition = "TEXT")
	private String introduction;

	@Column(columnDefinition = "TEXT")
	private String qualification;

	@Column(columnDefinition = "TEXT")
	private String preferential;

	@Column(columnDefinition = "TEXT")
	private String procedure;

	@Column(columnDefinition = "TEXT")
	private String benefit;

	@Column(columnDefinition = "TEXT")
	private String mainTask;
}

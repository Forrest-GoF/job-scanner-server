package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobDetail {

	@Column(columnDefinition = "TEXT")
	private String introduction;

	@Column(columnDefinition = "TEXT")
	private String qualification;

	@Column(columnDefinition = "TEXT")
	private String preferential;

	@Column(name = "procedures", columnDefinition = "TEXT")
	private String procedure;

	@Column(columnDefinition = "TEXT")
	private String benefit;

	@Column(columnDefinition = "TEXT")
	private String mainTask;

	@Builder
	public JobDetail(String introduction, String qualification, String preferential,
		String procedure, String benefit, String mainTask) {
		this.introduction = introduction;
		this.qualification = qualification;
		this.preferential = preferential;
		this.procedure = procedure;
		this.benefit = benefit;
		this.mainTask = mainTask;
	}
}

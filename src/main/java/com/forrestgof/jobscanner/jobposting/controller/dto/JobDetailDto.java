package com.forrestgof.jobscanner.jobposting.controller.dto;

import com.forrestgof.jobscanner.jobposting.domain.JobDetail;

import lombok.Data;

@Data
public class JobDetailDto {
	String summary;
	String introduction;
	String qualification;
	String preferential;
	String procedure;
	String benefit;
	String mainTask;

	public JobDetailDto(JobDetail jobDetail) {
		introduction = jobDetail.getIntroduction();
		qualification = jobDetail.getQualification();
		preferential = jobDetail.getPreferential();
		procedure = jobDetail.getProcedure();
		benefit = jobDetail.getBenefit();
		mainTask = jobDetail.getMainTask();
	}
}

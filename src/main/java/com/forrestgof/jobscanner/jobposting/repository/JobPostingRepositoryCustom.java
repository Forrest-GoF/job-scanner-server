package com.forrestgof.jobscanner.jobposting.repository;

import java.util.List;
import java.util.Map;

import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;

public interface JobPostingRepositoryCustom {

	List<JobPosting> findFilterJobs(JobSearchCondition jobSearchCondition);
}

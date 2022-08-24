package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;
import java.util.Map;

import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;

public interface JobPostingService {

	JobPosting save(JobPosting jobPosting);

	List<JobPosting> findAll();

	JobPosting findOne(Long id);

	List<JobPosting> findFilterJobs(JobSearchCondition jobSearchCondition);

	boolean existsByGoogleKey(String googleKey);

}

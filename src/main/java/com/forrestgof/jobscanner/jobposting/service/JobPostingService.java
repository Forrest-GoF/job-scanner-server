package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;
import java.util.Map;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;

public interface JobPostingService {
	JobPosting findOne(Long id);
	List<JobPosting> findAll();
	List<JobPosting> findFilterJobs(Map<String, Object> params);
}

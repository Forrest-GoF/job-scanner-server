package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.repository.JobPostingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultJobPostingService implements JobPostingService {

	private final JobPostingRepository jobPostingRepository;

	@Override
	@Transactional
	public JobPosting save(JobPosting jobPosting) {
		return jobPostingRepository.save(jobPosting);
	}

	@Override
	public List<JobPosting> findAll() {
		return jobPostingRepository.findAll();
	}

	@Override
	public JobPosting findOne(Long id) {
		return jobPostingRepository.findById(id)
			.orElseThrow();
	}

	@Override
	public List<JobPosting> findFilterJobs(JobSearchCondition jobSearchCondition) {
		return jobPostingRepository.findFilterJobs(jobSearchCondition);
	}

	@Override
	public boolean existsByGoogleKey(String googleKey) {
		return jobPostingRepository.existsByGoogleKey(googleKey);
	}
}

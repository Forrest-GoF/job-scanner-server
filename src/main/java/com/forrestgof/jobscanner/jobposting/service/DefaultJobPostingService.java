package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.repository.JobPostingCustomRepository;
import com.forrestgof.jobscanner.jobposting.repository.JobPostingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultJobPostingService implements JobPostingService {

	private final JobPostingRepository jobPostingRepository;
	private final JobPostingCustomRepository jobPostingCustomRepository;

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
	public List<JobPosting> findFilterJobs(Map<String, String> param, List<String> tags) {
		return jobPostingCustomRepository.findFilterJobs(param, tags);
	}

	@Override
	public boolean existsByGoogleKey(String googleKey) {
		return jobPostingRepository.existsByGoogleKey(googleKey);
	}
}

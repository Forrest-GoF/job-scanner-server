package com.forrestgof.jobscanner.jobposting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.domain.JobTag;
import com.forrestgof.jobscanner.jobposting.repository.JobTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobTagService {

	private final JobTagRepository jobTagRepository;

	@Transactional
	public JobTag save(JobTag jobTag) {
		return jobTagRepository.save(jobTag);
	}
}

package com.forrestgof.jobscanner.tag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.tag.domain.JobTag;
import com.forrestgof.jobscanner.tag.repository.JobTagRepository;

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

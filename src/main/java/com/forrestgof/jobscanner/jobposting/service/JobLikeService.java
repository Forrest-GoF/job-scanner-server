package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.controller.dto.JobLikeRequest;
import com.forrestgof.jobscanner.jobposting.domain.JobLike;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.repository.JobLikeRepository;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobLikeService {

	private final JobLikeRepository jobLikeRepository;

	public List<JobPosting> findLikeJobs(Member member) {
		return jobLikeRepository.findByMember(member)
			.stream()
			.filter(JobLike::isActivated)
			.map(JobLike::getJobPosting)
			.toList();
	}

	public boolean isActivated(JobPosting jobPosting, Member member) {
		return jobLikeRepository.findByJobPostingAndMember(jobPosting, member)
			.map(JobLike::isActivated)
			.orElse(false);
	}

	@Transactional
	public void updateLike(
		JobPosting jobPosting,
		Member member,
		JobLikeRequest request
	) {
		jobLikeRepository.findByJobPostingAndMember(jobPosting, member)
			.orElseGet(() -> jobLikeRepository.save(JobLike.of(jobPosting, member)))
			.update(request.activated());
	}
}

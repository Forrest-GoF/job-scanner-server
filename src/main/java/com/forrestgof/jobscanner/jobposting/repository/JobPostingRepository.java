package com.forrestgof.jobscanner.jobposting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long>,
JobPostingRepositoryCustom {

	boolean existsByGoogleKey(String key);
}

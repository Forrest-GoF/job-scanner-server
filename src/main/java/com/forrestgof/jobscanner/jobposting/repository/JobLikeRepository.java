package com.forrestgof.jobscanner.jobposting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.jobposting.domain.JobLike;

public interface JobLikeRepository extends JpaRepository<JobLike, JobLike.JobLikeId> {
}

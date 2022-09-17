package com.forrestgof.jobscanner.jobposting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.jobposting.domain.JobTag;

public interface JobTagRepository extends JpaRepository<JobTag, Long> {
}

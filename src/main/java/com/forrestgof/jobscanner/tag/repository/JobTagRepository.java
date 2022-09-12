package com.forrestgof.jobscanner.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.tag.domain.JobTag;

public interface JobTagRepository extends JpaRepository<JobTag, Long> {
}

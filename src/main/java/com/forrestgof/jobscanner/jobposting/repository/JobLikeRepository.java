package com.forrestgof.jobscanner.jobposting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.jobposting.domain.JobLike;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.member.domain.Member;

public interface JobLikeRepository extends JpaRepository<JobLike, Long> {

	List<JobLike> findByMember(Member member);

	Optional<JobLike> findByJobPostingAndMember(JobPosting jobPosting, Member member);
}

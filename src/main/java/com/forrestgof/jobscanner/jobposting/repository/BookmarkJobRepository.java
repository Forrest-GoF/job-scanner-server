package com.forrestgof.jobscanner.jobposting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.jobposting.domain.BookmarkJob;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.member.domain.Member;

public interface BookmarkJobRepository extends JpaRepository<BookmarkJob, Long> {

	List<BookmarkJob> findByMember(Member member);

	Optional<BookmarkJob> findByJobPostingAndMember(JobPosting jobPosting, Member member);
}

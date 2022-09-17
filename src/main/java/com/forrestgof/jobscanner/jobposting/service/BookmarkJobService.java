package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.controller.dto.BookmarkJobRequest;
import com.forrestgof.jobscanner.jobposting.domain.BookmarkJob;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.repository.BookmarkJobRepository;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkJobService {

	private final BookmarkJobRepository bookmarkJobRepository;

	public List<JobPosting> findBookmarkJobPosting(Member member) {
		return bookmarkJobRepository.findByMember(member)
			.stream()
			.filter(BookmarkJob::isActivated)
			.map(BookmarkJob::getJobPosting)
			.toList();
	}

	public boolean checkActivation(JobPosting jobPosting, Member member) {
		return bookmarkJobRepository.findByJobPostingAndMember(jobPosting, member)
			.map(BookmarkJob::isActivated)
			.orElse(false);
	}

	@Transactional
	public void updateLike(
		JobPosting jobPosting,
		Member member,
		BookmarkJobRequest request
	) {
		bookmarkJobRepository.findByJobPostingAndMember(jobPosting, member)
			.orElseGet(() -> bookmarkJobRepository.save(BookmarkJob.of(jobPosting, member)))
			.update(request.activated());
	}
}

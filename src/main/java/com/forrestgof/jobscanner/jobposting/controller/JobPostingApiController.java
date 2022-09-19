package com.forrestgof.jobscanner.jobposting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.BookmarkJobRequest;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobPreviewResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.service.BookmarkJobService;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.exception.MemberCustomException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobPostingApiController {

	private final JobPostingService jobPostingService;
	private final BookmarkJobService bookmarkJobService;
	private final AuthService authService;

	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<Result> getFilterJobs(
		HttpServletRequest request,
		JobSearchCondition jobSearchCondition
	) {
		List<JobPosting> findJobs = jobPostingService.findFilterJobs(jobSearchCondition);

		List<JobPreviewResponse> jobPreviewResponses = parseToDtoList(findJobs);

		Set<Long> bookmarkJobs = getMember(request)
			.map(bookmarkJobService::findBookmarkJobPosting)
			.orElseGet(ArrayList::new)
			.stream()
			.map(JobPosting::getId)
			.collect(Collectors.toSet());

		jobPreviewResponses.stream()
			.filter(jobPreviewResponse ->
				bookmarkJobs.contains(jobPreviewResponse.getId()))
			.forEach(JobPreviewResponse::activateBookmark);

		Result result = new Result(jobPreviewResponses, jobPreviewResponses.size());

		return CustomResponse.success(result);
	}

	record Result(
		List<JobPreviewResponse> jobs,
		int length
	) {

	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<JobResponse> getJob(
		HttpServletRequest request,
		@PathVariable Long id
	) {
		JobPosting jobPosting = jobPostingService.findOne(id);
		jobPosting.increaseViews();
		jobPostingService.save(jobPosting);

		JobResponse jobResponse = new JobResponse(jobPosting);

		getMember(request)
			.ifPresent(member -> jobResponse.activateBookmark());

		return CustomResponse.success(jobResponse);
	}


	@GetMapping("bookmarks")
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<List<JobPreviewResponse>> getBookmark(
		HttpServletRequest request
	) {
		Member member = getMember(request)
			.orElseThrow(MemberCustomException::notfound);

		List<JobPosting> likeJobs = bookmarkJobService.findBookmarkJobPosting(member);
		List<JobPreviewResponse> jobPreviewResponses = parseToDtoList(likeJobs);
		jobPreviewResponses.forEach(JobPreviewResponse::activateBookmark);

		return CustomResponse.success(jobPreviewResponses);
	}

	@PutMapping("bookmarks/{jobPostingId}")
	@ResponseStatus(HttpStatus.CREATED)
	public CustomResponse<BookmarkJobRequest> updateBookmark(
		HttpServletRequest request,
		@PathVariable Long jobPostingId,
		@RequestBody BookmarkJobRequest bookmarkJobRequest
	) {
		Member member = getMember(request)
			.orElseThrow(MemberCustomException::notfound);

		JobPosting jobPosting = jobPostingService.findOne(jobPostingId);

		bookmarkJobService.updateLike(jobPosting, member, bookmarkJobRequest);

		return CustomResponse.success(bookmarkJobRequest);
	}

	private List<JobPreviewResponse> parseToDtoList(List<JobPosting> find) {
		return find.stream()
			.map(JobPreviewResponse::new)
			.collect(Collectors.toList());
	}

	private Optional<Member> getMember(HttpServletRequest request) {
		try {
			String appToken = JwtHeaderUtil.getAccessToken(request);
			Member member = authService.getMemberFromAppToken(appToken);
			return Optional.of(member);
		} catch (AuthCustomException e) {
			return Optional.empty();
		}
	}
}

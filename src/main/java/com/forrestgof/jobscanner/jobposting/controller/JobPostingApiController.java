package com.forrestgof.jobscanner.jobposting.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobLikeRequest;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobPreviewResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.service.JobLikeService;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobPostingApiController {

	private final JobPostingService jobPostingService;
	private final JobLikeService jobLikeService;
	private final AuthService authService;

	@GetMapping("")
	public ResponseEntity<CustomResponse> getFilterJobs(JobSearchCondition jobSearchCondition) {
		List<JobPosting> findJobs = jobPostingService.findFilterJobs(jobSearchCondition);
		List<JobPreviewResponse> previewDtos = parseToDtoList(findJobs);

		return CustomResponse.success(new Result(previewDtos, previewDtos.size()));
	}

	record Result(
		List<JobPreviewResponse> jobs,
		int length
	) {

	}

	@GetMapping("{id}")
	public ResponseEntity<CustomResponse> getJob(
		HttpServletRequest request,
		@PathVariable Long id
	) {
		JobPosting jobPosting = jobPostingService.findOne(id);
		JobResponse jobResponse = new JobResponse(jobPosting);

		try {
			Member member = getMember(request);
			boolean activated = jobLikeService.isActivated(jobPosting, member);
			jobResponse.setLike(activated);
		} catch (AuthCustomException ignored) { }

		return CustomResponse.success(jobResponse);
	}


	@GetMapping("like")
	public ResponseEntity<CustomResponse> getLike(
		HttpServletRequest request
	) {
		Member member = getMember(request);

		List<JobPosting> likeJobs = jobLikeService.findLikeJobs(member);
		List<JobPreviewResponse> jobPreviewResponses = parseToDtoList(likeJobs);

		return CustomResponse.success(jobPreviewResponses);
	}

	@PutMapping("like/{jobPostingId}")
	public ResponseEntity<CustomResponse> updateLike(
		HttpServletRequest request,
		@PathVariable Long jobPostingId,
		@RequestBody JobLikeRequest jobLikeRequest
	) {
		Member member = getMember(request);
		JobPosting jobPosting = jobPostingService.findOne(jobPostingId);

		jobLikeService.updateLike(jobPosting, member, jobLikeRequest);

		return CustomResponse.success();
	}

	private List<JobPreviewResponse> parseToDtoList(List<JobPosting> find) {
		return find.stream()
			.map(JobPreviewResponse::new)
			.collect(Collectors.toList());
	}

	private Member getMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		return authService.getMemberFromAppToken(appToken);
	}
}

package com.forrestgof.jobscanner.jobposting.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobDto;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobPreviewDto;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobPostingApiController {

	private final JobPostingService jobPostingService;

	@GetMapping("")
	public ResponseEntity<Result> getFilterJobs(JobSearchCondition jobSearchCondition) {
		List<JobPosting> findJobs = jobPostingService.findFilterJobs(jobSearchCondition);
		List<JobPreviewDto> previewDtos = parseToDtoList(findJobs);

		return CustomResponse.success(new Result(previewDtos, previewDtos.size()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobDto> getJob(
		@PathVariable Long id
	) {
		JobPosting findOne = jobPostingService.findOne(id);

		return CustomResponse.success(new JobDto(findOne));
	}

	private List<JobPreviewDto> parseToDtoList(List<JobPosting> find) {
		return find.stream()
			.map(JobPreviewDto::new)
			.collect(Collectors.toList());
	}

	static record Result(
		List<JobPreviewDto> jobs,
		int length
	) {

	}
}

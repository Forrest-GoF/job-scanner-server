package com.forrestgof.jobscanner.jobposting.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobDto;
import com.forrestgof.jobscanner.jobposting.controller.dto.JobPreviewDto;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.service.JobPostingService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobPostingApiController {

	private final JobPostingService jobPostingService;

	@GetMapping("/api/jobs")
	@ResponseBody
	public ResponseEntity<Result> getFilterJobs(
		@RequestParam(required = false) Map<String, String> params
	) {
		List<JobPosting> all = jobPostingService.findFilterJobs(params);
		List<JobPreviewDto> previewDtos = all.stream()
			.map(JobPreviewDto::new)
			.collect(Collectors.toList());

		return CustomResponse.success(new Result(previewDtos, previewDtos.size()));
	}

	@GetMapping("api/jobs/{id}")
	@ResponseBody
	public ResponseEntity<JobDto> getJob(
		@PathVariable Long id
	) {
		JobPosting findOne = jobPostingService.findOne(id);
		return CustomResponse.success(new JobDto(findOne));
	}

	@Data
	@AllArgsConstructor
	static class Result {
		List<JobPreviewDto> jobs;
		int length;
	}
}

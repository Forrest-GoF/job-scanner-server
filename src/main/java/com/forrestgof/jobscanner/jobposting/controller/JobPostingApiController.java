package com.forrestgof.jobscanner.jobposting.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/joblist")
	@ResponseBody
	public Result getFilterJobs(
		@RequestParam(required = false) Map<String, Object> params
	) {
		List<JobPosting> all = jobPostingService.findFilterJobs(params);
		List<JobPreviewDto> previewDtos = all.stream()
			.map(JobPreviewDto::new)
			.collect(Collectors.toList());

		return new Result(previewDtos, previewDtos.size());
	}

	@GetMapping("/job/{id}")
	@ResponseBody
	public JobDto getJob(
		@PathVariable Long id
	) {
		JobPosting findOne = jobPostingService.findOne(id);
		return new JobDto(findOne);
	}

	@Data
	@AllArgsConstructor
	static class Result {
		List<JobPreviewDto> jobs;
		int length;
	}
}

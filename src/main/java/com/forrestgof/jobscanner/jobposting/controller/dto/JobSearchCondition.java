package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.jobposting.domain.JobType;

import lombok.Data;

@Data
public class JobSearchCondition {
	private long page = 1;
	private long size = 20;
	private long minEmployees;
	private long minSalary;
	private String sortedBy;
	private List<String> tags;
	private List<String> type;

	public long getMinSalary() {
		return minSalary*10000;
	}

	public long getOffset() {
		return page-1;
	}

	public long getLimit() {
		return size;
	}

	public List<JobType> getJobType() {
		if (type==null) {
			return null;
		}
		return type.stream()
			.map(JobType::valueOf)
			.collect(Collectors.toList());
	}

	public Optional<SortingCondition> getSoringCondition() {
		if (sortedBy==null) {
			return Optional.empty();
		}
		return Optional.of(SortingCondition.valueOf(sortedBy));
	}
}

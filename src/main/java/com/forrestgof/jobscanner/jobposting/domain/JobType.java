package com.forrestgof.jobscanner.jobposting.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobType {
	FULLTIME("정규직"),
	INTERNSHIP("인턴십"),
	PARTTIME("계약직"),
	CONTRACT("시간제");

	private final String name;

	public static JobType from(String typeNamedKorean) {
		return Arrays.stream(JobType.values())
			.filter(r -> r.getName().equals(typeNamedKorean))
			.findFirst()
			.orElse(null);
	}
}

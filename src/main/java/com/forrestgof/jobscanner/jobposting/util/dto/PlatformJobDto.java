package com.forrestgof.jobscanner.jobposting.util.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlatformJobDto {
	String benefit;
	String deadline;
	String introduction;
	String location;
	String mainTask;
	String preferential;
	String procedure;
	String qualification;
	List<String> stacks;
}

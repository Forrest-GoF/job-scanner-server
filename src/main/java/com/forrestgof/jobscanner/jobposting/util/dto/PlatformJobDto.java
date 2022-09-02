package com.forrestgof.jobscanner.jobposting.util.dto;

import java.util.List;

public record PlatformJobDto (
	String benefit,
	String deadline,
	String introduction,
	String location,
	String mainTask,
	String preferential,
	String procedure,
	String qualification,
	List<String> stacks
) {

}

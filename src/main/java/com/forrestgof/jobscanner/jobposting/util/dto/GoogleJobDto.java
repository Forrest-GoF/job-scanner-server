package com.forrestgof.jobscanner.jobposting.util.dto;

public record GoogleJobDto (
	String applyUrl,
	String companyName,
	String description,
	String key,
	String location,
	String platform,
	String postedAt,
	String salary,
	String thumbnail,
	String title,
	String type
) {

}

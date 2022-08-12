package com.forrestgof.jobscanner.jobposting.util.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleJobDto {
	String applyUrl;
	String companyName;
	String description;
	String key;
	String location;
	String platform;
	String postedAt;
	String salary;
	String thumbnail;
	String title;
	String type;
}

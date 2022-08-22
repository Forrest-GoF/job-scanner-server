package com.forrestgof.jobscanner.company.util.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NpsBassDto {
	String key;
	String name;
	String registrationNumber;
	String updatedAt;
	String seq;
	String address;
	String registrationStatus;
	String registrationType;
	String countyCode;
	String cityCode;
	String townCode;
}

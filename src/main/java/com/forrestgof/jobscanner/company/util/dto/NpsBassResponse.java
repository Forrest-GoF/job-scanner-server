package com.forrestgof.jobscanner.company.util.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NpsBassResponse {
	int length;
	List<NpsBassDto> data;
}

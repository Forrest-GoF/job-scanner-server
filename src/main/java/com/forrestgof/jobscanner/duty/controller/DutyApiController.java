package com.forrestgof.jobscanner.duty.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.service.DutyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("duties")
@RequiredArgsConstructor
public class DutyApiController {

	private final DutyService dutyService;

	@GetMapping
	public ResponseEntity<CustomResponse> getDuties() {
		List<String> duties = dutyService.findAll()
			.stream()
			.map(Duty::getName)
			.toList();

		return CustomResponse.success(duties);
	}
}

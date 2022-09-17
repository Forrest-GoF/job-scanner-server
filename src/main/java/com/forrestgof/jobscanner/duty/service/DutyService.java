package com.forrestgof.jobscanner.duty.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.repository.DutyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DutyService {

	private final DutyRepository dutyRepository;

	public List<Duty> findAll() {
		return dutyRepository.findAll();
	}
}

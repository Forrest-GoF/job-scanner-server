package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.domain.ObjectiveCompany;
import com.forrestgof.jobscanner.member.repository.ObjectiveCompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObjectiveCompanyService {

	private final ObjectiveCompanyRepository objectiveCompanyRepository;

	public Optional<ObjectiveCompany> findByMember(Member member) {
		return objectiveCompanyRepository.findById(member.getId());
	}
}

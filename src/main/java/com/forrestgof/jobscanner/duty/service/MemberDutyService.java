package com.forrestgof.jobscanner.duty.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.domain.MemberDuty;
import com.forrestgof.jobscanner.duty.repository.MemberDutyRepository;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDutyService {

	private final MemberDutyRepository memberDutyRepository;

	public List<Duty> findDutiesFromMember(Member member) {
		List<MemberDuty> memberDuties = memberDutyRepository.findByMember(member);

		return memberDuties.stream()
			.map(MemberDuty::getDuty)
			.collect(Collectors.toList());
	}
}

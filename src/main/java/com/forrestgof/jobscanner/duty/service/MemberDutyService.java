package com.forrestgof.jobscanner.duty.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.domain.MemberDuty;
import com.forrestgof.jobscanner.duty.repository.DutyRepository;
import com.forrestgof.jobscanner.duty.repository.MemberDutyRepository;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDutyService {

	private final MemberDutyRepository memberDutyRepository;
	private final DutyRepository dutyRepository;

	public List<Duty> findDutiesFromMember(Member member) {
		List<MemberDuty> memberDuties = memberDutyRepository.findByMember(member);

		return memberDuties.stream()
			.map(MemberDuty::getDuty)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateMemberDuty(Member member, List<String> duties) {
		List<MemberDuty> findDuties = memberDutyRepository.findByMember(member);

		memberDutyRepository.deleteAll(findDuties);

		List<MemberDuty> updatedMemberDuties = duties.stream()
			.map(dutyRepository::findByName)
			.map(Optional::orElseThrow)
			.map(duty -> MemberDuty.of(member, duty))
			.map(memberDutyRepository::save)
			.toList();

		member.updateMemberDuties(updatedMemberDuties);
	}
}

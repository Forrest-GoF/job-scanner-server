package com.forrestgof.jobscanner.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.domain.MemberDuty;
import com.forrestgof.jobscanner.duty.repository.DutyRepository;
import com.forrestgof.jobscanner.member.repository.MemberDutyRepository;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDutyService {

	private final MemberDutyRepository memberDutyRepository;
	private final DutyRepository dutyRepository;

	@Transactional
	public void updateMemberDuty(Member member, List<String> duties) {
		deleteMemberDuty(member);

		List<MemberDuty> memberDuties = duties.stream()
			.map(dutyRepository::findByName)
			.map(Optional::orElseThrow)
			.map(duty -> MemberDuty.of(member, duty))
			.map(memberDutyRepository::save)
			.toList();

		member.updateMemberDuties(memberDuties);
	}

	@Transactional
	public void deleteMemberDuty(Member member) {
		List<MemberDuty> findDuties = memberDutyRepository.findByMember(member);

		memberDutyRepository.deleteAll(findDuties);
	}
}

package com.forrestgof.jobscanner.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.duty.domain.MemberDuty;
import com.forrestgof.jobscanner.member.domain.Member;

public interface MemberDutyRepository extends JpaRepository<MemberDuty, Long> {

	List<MemberDuty> findByMember(Member member);
}

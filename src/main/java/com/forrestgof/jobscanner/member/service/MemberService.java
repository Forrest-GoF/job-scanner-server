package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import com.forrestgof.jobscanner.member.domain.Member;

public interface MemberService {

	Long save(Member member);

	Member findOne(Long id);

	Optional<Member> findByEmail(String email);
}

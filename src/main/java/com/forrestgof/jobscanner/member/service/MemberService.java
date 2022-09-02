package com.forrestgof.jobscanner.member.service;

import com.forrestgof.jobscanner.member.domain.Member;

public interface MemberService {

	Long save(Member member);

	Member findByEmail(String email);
}

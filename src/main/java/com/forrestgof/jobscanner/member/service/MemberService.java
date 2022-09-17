package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import com.forrestgof.jobscanner.member.controller.dto.MemberPatchRequest;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.dto.MemberSignInDto;
import com.forrestgof.jobscanner.member.dto.MemberSignUpDto;

public interface MemberService {

	Long save(Member member);

	Member findOne(Long id);

	Optional<Member> findByEmail(String email);

	Member signUp(MemberSignUpDto memberSignUpDto);

	Member signIn(MemberSignInDto memberSignInDto);

	void authenticateEmail(String email);

	void updateMember(Long id, MemberPatchRequest memberUpdateRequest);

	void deleteMember(Long memberId);
}

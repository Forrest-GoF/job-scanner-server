package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import com.forrestgof.jobscanner.member.controller.dto.MemberPatchRequest;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.auth.controller.dto.SignInRequest;
import com.forrestgof.jobscanner.auth.controller.dto.SignUpRequest;

public interface MemberService {

	Long save(Member member);

	Member findOne(Long id);

	Optional<Member> findByEmail(String email);

	Member signUp(SignUpRequest signUpRequest);

	Member signIn(SignInRequest memberSignInDto);

	void authenticateEmail(String email);

	void updateMember(Long id, MemberPatchRequest memberUpdateRequest);

	void deleteMember(Long memberId);
}

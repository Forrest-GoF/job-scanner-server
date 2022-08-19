package com.forrestgof.jobscanner.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long join(Member member) {
		if (memberRepository.existsByEmail(member.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
		Member findMember = memberRepository.save(member);
		return findMember.getId();
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
	}
}

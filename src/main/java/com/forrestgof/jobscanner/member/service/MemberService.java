package com.forrestgof.jobscanner.member.service;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.repository.MemberRepository;
import com.forrestgof.jobscanner.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final SessionRepository sessionRepository;

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

	public Member findByAppTokenUuid(String uuid) {
		return sessionRepository
			.findByAppTokenUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN_EXCEPTION))
			.getMember();
	}
}

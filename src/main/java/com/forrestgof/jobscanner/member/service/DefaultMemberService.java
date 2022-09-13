package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.common.exception.UndefinedException;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.dto.MemberSignInDto;
import com.forrestgof.jobscanner.member.dto.MemberSignUpDto;
import com.forrestgof.jobscanner.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultMemberService implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long save(Member member) {
		if (memberRepository.existsByEmail(member.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
		Member findMember = memberRepository.save(member);
		return findMember.getId();
	}

	@Override
	public Member findOne(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	@Override
	public Member signUp(MemberSignUpDto memberSignUpDto) {
		Member member = memberSignUpDto.toEntity();
		member.encodePassword(passwordEncoder);
		Long memberId = save(member);
		return findOne(memberId);
	}

	@Override
	public Member signIn(MemberSignInDto memberSignInDto) {
		Member member = memberSignInDto.toEntity();
		Member findMember = findByEmail(member.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
		if (!passwordEncoder.matches(member.getPassword(), findMember.getPassword())) {
			throw new UndefinedException("The password is incorrect.");
		}
		return findMember;
	}

	@Override
	@Transactional
	public void authenticateEmail(String email) {
		Member findMember = memberRepository.findByEmail(email)
			.orElseThrow();

		findMember.authenticateEmail();
	}
}

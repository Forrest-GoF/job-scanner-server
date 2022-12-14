package com.forrestgof.jobscanner.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.member.controller.dto.MemberPatchRequest;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.auth.controller.dto.SignInRequest;
import com.forrestgof.jobscanner.auth.controller.dto.SignUpRequest;
import com.forrestgof.jobscanner.member.exception.MemberCustomException;
import com.forrestgof.jobscanner.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultMemberService implements MemberService {

	private final MemberRepository memberRepository;
	private final MemberTagService memberTagService;
	private final MemberDutyService memberDutyService;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long save(Member member) {
		if (memberRepository.existsByEmail(member.getEmail())) {
			throw new MemberCustomException("Email already exists");
		}
		Member findMember = memberRepository.save(member);
		return findMember.getId();
	}

	@Override
	public Member findOne(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(MemberCustomException::notfound);
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	@Override
	public Member signUp(SignUpRequest signUpRequest) {
		Member member = Member.builder()
			.email(signUpRequest.email())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.nickname(signUpRequest.nickname())
			.promotionAgreement(signUpRequest.promotionAgreement())
			.build();

		Long memberId = save(member);

		return findOne(memberId);
	}

	@Override
	public Member signIn(SignInRequest memberSignInDto) {
		Member findMember = findByEmail(memberSignInDto.email())
			.orElseThrow(MemberCustomException::notfound);

		if (!passwordEncoder.matches(memberSignInDto.password(), findMember.getPassword())) {
			throw new MemberCustomException("The password is incorrect");
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

	@Override
	@Transactional
	public void updateMember(Long id, MemberPatchRequest memberUpdateRequest) {
		Member findMember = memberRepository.findById(id)
			.orElseThrow(MemberCustomException::notfound);

		String nickName = memberUpdateRequest.nickname()
			.orElseGet(findMember::getNickname);

		String imageUrl = memberUpdateRequest.imageUrl()
			.orElseGet(findMember::getImageUrl);

		boolean promotionAgreement = memberUpdateRequest.promotionAgreement()
			.orElseGet(findMember::isPromotionAgreement);

		findMember.updateMember(
			nickName,
			imageUrl,
			promotionAgreement
		);

		memberUpdateRequest.tags()
				.ifPresent(tags -> memberTagService.updateMemberTag(findMember, tags));

		memberUpdateRequest.duties()
				.ifPresent(duties -> memberDutyService.updateMemberDuty(findMember, duties));
	}

	@Override
	@Transactional
	public void deleteMember(Long memberId) {
		Member findMember = memberRepository.findById(memberId)
			.orElseThrow(MemberCustomException::notfound);

		memberTagService.deleteMemberTag(findMember);
		memberDutyService.deleteMemberDuty(findMember);
		memberRepository.delete(findMember);
	}
}

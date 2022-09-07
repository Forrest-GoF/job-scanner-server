package com.forrestgof.jobscanner.socialmember.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.repository.SocialMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultSocialMemberService implements SocialMemberService {

	private final SocialMemberRepository socialMemberRepository;

	@Transactional
	public Long save(SocialMember socialMember) {
		if (socialMemberRepository.existsByEmailAndType(socialMember.getEmail(), socialMember.getType())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
		}
		SocialMember findSocialMember = socialMemberRepository.save(socialMember);
		return findSocialMember.getId();
	}

	@Override
	public Optional<SocialMember> findByEmailAndType(String email, String type) {
		return socialMemberRepository.findByEmailAndType(email, type);
	}
}

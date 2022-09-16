package com.forrestgof.jobscanner.socialmember.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.domain.SocialType;
import com.forrestgof.jobscanner.socialmember.exception.SocialMemberCustomException;
import com.forrestgof.jobscanner.socialmember.repository.SocialMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultSocialMemberService implements SocialMemberService {

	private final SocialMemberRepository socialMemberRepository;

	@Override
	@Transactional
	public Long save(SocialMember socialMember) {
		if (socialMemberRepository.existsByEmailAndSocialType(socialMember.getEmail(), socialMember.getSocialType())) {
			throw new SocialMemberCustomException("Already joined member", HttpStatus.CONFLICT);
		}

		SocialMember findSocialMember = socialMemberRepository.save(socialMember);
		return findSocialMember.getId();
	}

	@Override
	public SocialMember findOne(Long id) {
		return socialMemberRepository.findById(id)
			.orElseThrow(() -> new SocialMemberCustomException("Unsigned account", HttpStatus.UNAUTHORIZED));
	}

	@Override
	public Optional<SocialMember> findByEmailAndSocialType(String email, SocialType socialType) {
		return socialMemberRepository.findByEmailAndSocialType(email, socialType);
	}
}

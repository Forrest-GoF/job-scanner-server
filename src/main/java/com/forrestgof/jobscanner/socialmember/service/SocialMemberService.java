package com.forrestgof.jobscanner.socialmember.service;

import java.util.Optional;

import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.domain.SocialType;

public interface SocialMemberService {

	Long save(SocialMember socialMember);

	Optional<SocialMember> findByEmailAndSocialType(String email, SocialType socialType);
}

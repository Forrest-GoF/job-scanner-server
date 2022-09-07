package com.forrestgof.jobscanner.socialmember.service;

import java.util.Optional;

import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

public interface SocialMemberService {

	Long save(SocialMember socialMember);

	Optional<SocialMember> findByEmailAndType(String email, String type);
}

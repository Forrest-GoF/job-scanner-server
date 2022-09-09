package com.forrestgof.jobscanner.socialmember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.domain.SocialType;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

	Optional<SocialMember> findByEmailAndSocialType(String email, SocialType socialType);

	boolean existsByEmailAndSocialType(String email, SocialType socialType);
}

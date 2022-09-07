package com.forrestgof.jobscanner.socialmember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

	SocialMember save(SocialMember socialMember);

	Optional<SocialMember> findByEmailAndType(String email, String type);

	boolean existsByEmailAndType(String email, String type);
}

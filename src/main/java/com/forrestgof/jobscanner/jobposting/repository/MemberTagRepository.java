package com.forrestgof.jobscanner.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.tag.domain.MemberTag;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

	List<MemberTag> findByMember(Member member);
}


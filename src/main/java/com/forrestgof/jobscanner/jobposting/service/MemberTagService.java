package com.forrestgof.jobscanner.tag.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.tag.domain.MemberTag;
import com.forrestgof.jobscanner.tag.domain.Tag;
import com.forrestgof.jobscanner.tag.repository.MemberTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberTagService {

	private final MemberTagRepository memberTagRepository;

	public List<Tag> findTagsFromMember(Member member) {
		List<MemberTag> memberTags = memberTagRepository.findByMember(member);

		return memberTags.stream()
			.map(MemberTag::getTag)
			.collect(Collectors.toList());
	}
}

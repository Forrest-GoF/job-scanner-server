package com.forrestgof.jobscanner.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.domain.MemberTag;
import com.forrestgof.jobscanner.tag.domain.Tag;
import com.forrestgof.jobscanner.member.repository.MemberTagRepository;
import com.forrestgof.jobscanner.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberTagService {

	private final MemberTagRepository memberTagRepository;
	private final TagRepository tagRepository;

	public List<Tag> findTagsFromMember(Member member) {
		List<MemberTag> memberTags = memberTagRepository.findByMember(member);

		return memberTags.stream()
			.map(MemberTag::getTag)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateMemberTag(Member member, List<String> tags) {
		List<MemberTag> memberTags = memberTagRepository.findByMember(member);

		memberTagRepository.deleteAll(memberTags);

		List<MemberTag> updatedMemberTags = tags.stream()
			.map(tagRepository::findByName)
			.map(Optional::orElseThrow)
			.map(tag -> MemberTag.of(member, tag))
			.map(memberTagRepository::save)
			.toList();

		member.updateMemberTags(memberTags);
	}
}

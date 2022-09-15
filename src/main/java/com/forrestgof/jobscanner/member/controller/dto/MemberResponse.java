package com.forrestgof.jobscanner.member.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.domain.MemberDuty;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.tag.domain.MemberTag;
import com.forrestgof.jobscanner.tag.domain.Tag;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponse {

	private String email;
	private String nickname;
	private String imageUrl;

	private List<String> tags;
	private List<String> duties;

	public static MemberResponse from(Member member) {
		MemberResponse memberResponse = new MemberResponse();

		memberResponse.email = member.getEmail();
		memberResponse.nickname = member.getNickname();
		memberResponse.imageUrl = member.getImageUrl();
		memberResponse.tags = member.getMemberTags()
			.stream()
			.map(MemberTag::getTag)
			.map(Tag::getName)
			.collect(Collectors.toList());
		memberResponse.duties = member.getMemberDuties()
			.stream()
			.map(MemberDuty::getDuty)
			.map(Duty::getName)
			.collect(Collectors.toList());

		return memberResponse;
	}
}

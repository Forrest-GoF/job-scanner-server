package com.forrestgof.jobscanner.member.dto;

import com.forrestgof.jobscanner.member.domain.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

	private String email;
	private String nickname;
	private String imageUrl;

	public static MemberResponse of(Member member) {
		return new MemberResponse(member.getEmail(), member.getNickname(), member.getImageUrl());
	}
}

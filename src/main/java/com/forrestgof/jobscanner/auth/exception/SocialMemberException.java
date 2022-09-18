package com.forrestgof.jobscanner.auth.exception;

import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.socialmember.domain.SocialType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialMemberException extends RuntimeException {
	Member member;
	SocialType socialType;
}

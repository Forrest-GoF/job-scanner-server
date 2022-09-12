package com.forrestgof.jobscanner.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.forrestgof.jobscanner.member.domain.Member;

public record MemberSignInDto(
	@Email
	String email,

	@NotBlank
	String password
) {

	public Member toEntity() {
		return Member.builder()
			.email(this.email)
			.password(this.password)
			.build();
	}
}

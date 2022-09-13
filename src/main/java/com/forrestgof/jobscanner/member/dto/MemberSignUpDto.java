package com.forrestgof.jobscanner.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.forrestgof.jobscanner.member.domain.Member;

public record MemberSignUpDto(
	@Email
	String email,

	@NotBlank
	String password,

	@NotNull
	String nickname
) {

	public Member toEntity() {
		return Member.builder()
			.email(this.email)
			.password(this.password)
			.nickname(this.nickname)
			.build();
	}
}

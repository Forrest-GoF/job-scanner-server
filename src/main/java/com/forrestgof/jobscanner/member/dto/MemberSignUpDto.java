package com.forrestgof.jobscanner.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record MemberSignUpDto(
	@Email
	String email,

	@NotBlank
	String password,

	@NotNull
	String nickname
) {
}

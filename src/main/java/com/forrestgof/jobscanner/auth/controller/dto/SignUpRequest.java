package com.forrestgof.jobscanner.auth.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record SignUpRequest(
	@Email
	String email,

	@NotBlank
	String password,

	@NotNull
	String nickname,

	@Valid
	Agreement agreement

) {
	public boolean promotionAgreement() {
		return agreement.promotion();
	}

	record Agreement(
		@AssertTrue
		boolean terms,

		@AssertTrue
		boolean personalInformation,

		boolean promotion
	) {

	}
}

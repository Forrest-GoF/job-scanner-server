package com.forrestgof.jobscanner.auth.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SignInRequest(
	@Email
	String email,

	@NotBlank
	String password
) {

}

package com.forrestgof.jobscanner.auth.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record SignUpRequest(
	@Email(message = "올바르지 않은 이메일 형식입니다.")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,16}$",
		message = "비밀번호는 8~16 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
	String password,

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(
		regexp = "[ㄱ-ㅎ가-힣A-Za-z\\d]{2,10}",
		message = "닉네임은 2~10 자리이면서 한글, 알파벳, 숫자만 가능합니다.")
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

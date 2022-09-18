package com.forrestgof.jobscanner.member.controller.dto;

import java.util.List;
import java.util.Optional;

public record MemberPatchRequest(
	Optional<String> nickname,
	Optional<String> imageUrl,
	Optional<List<String>> tags,
	Optional<List<String>> duties,

	Optional<Boolean> promotionAgreement
) {

}

package com.forrestgof.jobscanner.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.member.controller.dto.MemberResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.controller.dto.MemberPatchRequest;
import com.forrestgof.jobscanner.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

	private final AuthService authService;
	private final MemberService memberService;

	@GetMapping("")
	public ResponseEntity<CustomResponse> getMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);
		MemberResponse memberResponse = MemberResponse.from(member);

		return CustomResponse.success(memberResponse);
	}

	@PatchMapping("")
	public ResponseEntity<CustomResponse> patchMember(
		HttpServletRequest request,
		@RequestBody MemberPatchRequest memberUpdateRequest
	) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);
		memberService.updateMember(member.getId(), memberUpdateRequest);

		return CustomResponse.success();
	}
}

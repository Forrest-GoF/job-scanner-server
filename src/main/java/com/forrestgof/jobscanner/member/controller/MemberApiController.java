package com.forrestgof.jobscanner.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<MemberResponse> getMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);

		MemberResponse memberResponse = MemberResponse.from(member);

		return CustomResponse.success(memberResponse);
	}

	@PatchMapping("")
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<?> patchMember(
		HttpServletRequest request,
		@RequestBody MemberPatchRequest memberUpdateRequest
	) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);

		memberService.updateMember(member.getId(), memberUpdateRequest);

		return CustomResponse.success();
	}

	@DeleteMapping("")
	@ResponseStatus(HttpStatus.OK)
	public CustomResponse<?> deleteMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);

		authService.deleteSession(member);
		memberService.deleteMember(member.getId());

		return CustomResponse.success();
	}
}

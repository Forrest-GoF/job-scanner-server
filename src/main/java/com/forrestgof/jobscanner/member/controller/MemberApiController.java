package com.forrestgof.jobscanner.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.member.controller.dto.MemberResponse;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

	private final AuthService authService;

	@GetMapping("")
	public ResponseEntity<CustomResponse> getMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);
		MemberResponse memberResponse = MemberResponse.from(member);

		return CustomResponse.success(memberResponse);
	}
}

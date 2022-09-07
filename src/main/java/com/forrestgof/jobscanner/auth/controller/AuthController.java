package com.forrestgof.jobscanner.auth.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.forrestgof.jobscanner.auth.dto.AuthLoginResponse;
import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.auth.social.SocialTokenValidator;
import com.forrestgof.jobscanner.auth.social.SocialTokenValidatorFactory;
import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.service.SocialMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final SocialMemberService socialMemberService;
	private final AuthService authService;
	private final SocialTokenValidatorFactory socialTokenValidatorFactory;
	private SocialTokenValidator socialTokenValidator;

	@PostMapping("signin/{socialType}")
	public ResponseEntity<AuthLoginResponse> socialSignin(
		@PathVariable("socialType") String socialType,
		HttpServletRequest request) {
		HttpStatus httpStatus = HttpStatus.OK;
		socialTokenValidator = socialTokenValidatorFactory.find(socialType);
		String code = JwtHeaderUtil.getCode(request);
		Member member = socialTokenValidator.generateMemberFromCode(code);
		Optional<SocialMember> findSocialMember = socialMemberService.findByEmailAndType(member.getEmail(), socialType);
		if (findSocialMember.isEmpty()) {
			findSocialMember = Optional.ofNullable(socialSignup(member, socialType));
			httpStatus = HttpStatus.CREATED;
		}
		return ResponseEntity.status(httpStatus).body(authService.signin(findSocialMember.get()));
	}

	private SocialMember socialSignup(Member member, String socialTYpe) {
		Member findMember = memberService.findByEmail(member.getEmail())
			.orElseGet(() -> signup(member));
		SocialMember socialMember = SocialMember.builder()
			.member(findMember)
			.email(member.getEmail())
			.type(socialTYpe)
			.build();
		socialMemberService.save(socialMember);
		return socialMemberService.findByEmailAndType(socialMember.getEmail(), socialMember.getType()).get();
	}

	private Member signup(Member member) {
		memberService.save(member);
		return memberService.findByEmail(member.getEmail()).get();
	}

	@PostMapping("refresh")
	public ResponseEntity<AuthRefreshResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		String refreshToken = JwtHeaderUtil.getRefreshToken(request);
		return CustomResponse.success(authService.refreshToken(appToken, refreshToken));
	}
}

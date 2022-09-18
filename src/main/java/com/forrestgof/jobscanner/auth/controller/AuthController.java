package com.forrestgof.jobscanner.auth.controller;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.forrestgof.jobscanner.auth.controller.dto.SignInRequest;
import com.forrestgof.jobscanner.auth.controller.dto.SignUpRequest;
import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.auth.service.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.service.dto.AuthTokenResponse;
import com.forrestgof.jobscanner.auth.social.SocialService;
import com.forrestgof.jobscanner.auth.social.SocialServiceFactory;
import com.forrestgof.jobscanner.common.config.properties.DomainProperties;
import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;
import com.forrestgof.jobscanner.socialmember.domain.SocialType;
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
	private final SocialServiceFactory socialServiceFactory;
	private final DomainProperties domainProperties;

	@PostMapping("signup")
	public ResponseEntity<CustomResponse> signUp(
		@RequestBody
		@Valid
		SignUpRequest signUpRequest
	) {
		Member createdMember = memberService.signUp(signUpRequest);

		AuthTokenResponse authTokenResponse = authService.signIn(createdMember);

		CustomResponse customResponse = CustomResponse.builder()
			.status(true)
			.data(authTokenResponse)
			.build();

		authService.sendAuthenticationMail(createdMember);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(customResponse);
	}

	@PostMapping("signin")
	public ResponseEntity<CustomResponse> signIn(
		@RequestBody
		@Valid
		SignInRequest signInRequest
	) {
		Member findMember = memberService.signIn(signInRequest);
		return CustomResponse.success(authService.signIn(findMember));
	}

	@GetMapping("mail/authenticate/{email}/{appToken}")
	public void authenticateMail(
		@PathVariable String email,
		@PathVariable String appToken,
		HttpServletResponse httpServletResponse) throws IOException {

		authService.validateMailWithAppToken(email, appToken);

		memberService.authenticateEmail(email);

		httpServletResponse.sendRedirect(domainProperties.webSite());
	}

	@GetMapping("signin/{socialType}")
	public void socialRedirect(
		@PathVariable("socialType") String type,
		HttpServletResponse httpServletResponse) throws IOException {

		SocialType socialType = SocialType.getEnum(type);
		SocialService socialService = socialServiceFactory.find(socialType);

		httpServletResponse.sendRedirect(socialService.getRedirectUrl());
	}

	@GetMapping("signin/callback/{socialType}")
	public ResponseEntity<CustomResponse> socialSignIn(
		@PathVariable("socialType") String type,
		@RequestParam String code
	) {
		AtomicReference<HttpStatus> httpStatus = new AtomicReference<>(HttpStatus.OK);

		SocialType socialType = SocialType.getEnum(type);
		SocialService socialService = socialServiceFactory.find(socialType);

		Member member = socialService.generateMemberFromCode(code);

		SocialMember findSocialMember = socialMemberService.findByEmailAndSocialType(member.getEmail(), socialType)
			.orElseGet(() -> {
				httpStatus.set(HttpStatus.CREATED);
				return socialSignUp(member, socialType);
			});

		AuthTokenResponse authTokenResponse = authService.signIn(findSocialMember);

		return CustomResponse.success(authTokenResponse, httpStatus.get());
	}

	private SocialMember socialSignUp(Member member, SocialType socialTYpe) {
		Member findMember = memberService.findByEmail(member.getEmail())
			.orElseGet(() -> {
				Long memberId = memberService.save(member);
				return memberService.findOne(memberId);
			});

		SocialMember socialMember = SocialMember.builder()
			.member(findMember)
			.email(member.getEmail())
			.socialType(socialTYpe)
			.build();

		Long socialMemberId = socialMemberService.save(socialMember);
		return socialMemberService.findOne(socialMemberId);
	}

	@PostMapping("refresh")
	public ResponseEntity<CustomResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		String refreshToken = JwtHeaderUtil.getRefreshToken(request);

		AuthRefreshResponse authRefreshResponse = authService.refreshToken(appToken, refreshToken);
		return CustomResponse.success(authRefreshResponse);
	}
}

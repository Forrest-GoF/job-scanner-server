package com.forrestgof.jobscanner.auth.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.controller.dto.SignInRequest;
import com.forrestgof.jobscanner.auth.controller.dto.SignUpRequest;
import com.forrestgof.jobscanner.auth.exception.SocialMemberException;
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
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthApiController {

	private final MemberService memberService;
	private final SocialMemberService socialMemberService;
	private final AuthService authService;
	private final SocialServiceFactory socialServiceFactory;
	private final DomainProperties domainProperties;

	@PostMapping("signup")
	@ResponseStatus(HttpStatus.CREATED)
	public CustomResponse<AuthTokenResponse> signUp(
		@RequestBody
		@Valid
		SignUpRequest signUpRequest
	) {
		Member createdMember = memberService.signUp(signUpRequest);

		AuthTokenResponse authTokenResponse = authService.signIn(createdMember);

		authService.sendAuthenticationMail(createdMember);

		return CustomResponse.success(authTokenResponse);
	}

	@GetMapping("mail/authenticate/{email}/{appToken}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void authenticateMail(
		@PathVariable String email,
		@PathVariable String appToken,
		HttpServletResponse response
	) throws IOException {

		authService.validateMailWithAppToken(email, appToken);
		memberService.authenticateEmail(email);

		response.sendRedirect(domainProperties.webSite());
	}

	@PostMapping("signin")
	public CustomResponse<AuthTokenResponse> signIn(
		@RequestBody
		@Valid
		SignInRequest signInRequest
	) {
		Member findMember = memberService.signIn(signInRequest);

		AuthTokenResponse authTokenResponse = authService.signIn(findMember);

		return CustomResponse.success(authTokenResponse);
	}

	@GetMapping("signin/{socialType}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void socialRedirect(
		@PathVariable("socialType") String type,
		HttpServletResponse response
	) throws IOException {

		SocialType socialType = SocialType.getEnum(type);
		SocialService socialService = socialServiceFactory.find(socialType);

		response.sendRedirect(socialService.getRedirectUrl());
	}

	@GetMapping("signin/callback/{socialType}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void socialSignIn(
		@PathVariable("socialType") String type,
		@RequestParam String code,
		HttpServletResponse response
	) throws IOException {

		SocialType socialType = SocialType.getEnum(type);
		SocialService socialService = socialServiceFactory.find(socialType);

		Member member = socialService.generateMemberFromCode(code);

		SocialMember findSocialMember = socialMemberService.findByEmailAndSocialType(member.getEmail(), socialType)
			.orElseThrow(() -> new SocialMemberException(member, socialType, response));

		AuthTokenResponse authTokenResponse = authService.signIn(findSocialMember);

		Cookie refreshTokenCookie = authService.getCookieFromRefreshToken(authTokenResponse.getRefreshToken());
		response.addCookie(refreshTokenCookie);
		response.sendRedirect(domainProperties.webSite() +
			"/oauth/callback/" + socialType.getName() +
			"?appToken=" + authTokenResponse.getAppToken());
	}

	@ExceptionHandler(SocialMemberException.class)
	@ResponseStatus(HttpStatus.CREATED)
	public CustomResponse<AuthTokenResponse> handleException(SocialMemberException e) throws IOException {
		SocialMember findSocialMember = socialSignUp(e.getMember(), e.getSocialType());

		AuthTokenResponse authTokenResponse = authService.signIn(findSocialMember);

		HttpServletResponse response = e.getResponse();
		response.sendRedirect(
			domainProperties.webSite() + "/oauth/callback/" + e.getSocialType().getName());

		return CustomResponse.success(authTokenResponse);
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
	@ResponseStatus(HttpStatus.CREATED)
	public CustomResponse<AuthRefreshResponse> refreshToken(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);
		String refreshToken = JwtHeaderUtil.getRefreshToken(request);

		AuthRefreshResponse authRefreshResponse = authService.refreshToken(appToken, refreshToken);

		return CustomResponse.success(authRefreshResponse);
	}
}

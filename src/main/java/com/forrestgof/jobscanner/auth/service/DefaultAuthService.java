package com.forrestgof.jobscanner.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.auth.dto.AuthRefreshResponse;
import com.forrestgof.jobscanner.auth.dto.AuthTokenResponse;
import com.forrestgof.jobscanner.auth.exception.AuthCustomException;
import com.forrestgof.jobscanner.auth.jwt.AuthToken;
import com.forrestgof.jobscanner.auth.jwt.AuthTokenProvider;
import com.forrestgof.jobscanner.mail.service.MailService;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.session.domain.Session;
import com.forrestgof.jobscanner.session.repository.SessionRepository;
import com.forrestgof.jobscanner.session.service.SessionService;
import com.forrestgof.jobscanner.socialmember.domain.SocialMember;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

	private final SessionService sessionService;
	private final SessionRepository sessionRepository;
	private final AuthTokenProvider authTokenProvider;
	private final MailService mailService;

	@Override
	public AuthTokenResponse signIn(Member findMember) {
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();

		Session session = Session.builder()
			.member(findMember)
			.appTokenUuid(appTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();
		sessionService.save(session);

		AuthToken authToken = authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);

		return AuthTokenResponse.of(
			authToken.getAppToken(),
			authToken.getRefreshToken());
	}

	@Override
	public AuthTokenResponse signIn(SocialMember socialMember) {
		Member member = socialMember.getMember();
		return signIn(member);
	}

	@Override
	public void validateMailWithAppToken(String email, String appToken) {
		Member findMember = getMemberFromAppToken(appToken);
		if (!email.equals(findMember.getEmail())) {
			throw new AuthCustomException("Invalid email authentication");
		}
	}

	@Override
	public void sendAuthenticationMail(Member member) {
		Session session = sessionService.findByMember(member);

		String newAppTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = session.getRefreshTokenUuid();

		Session newSession = Session.builder()
			.member(member)
			.appTokenUuid(newAppTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();

		sessionService.save(newSession);

		String newAppToken = authTokenProvider.createUserAuthToken(newAppTokenUuid, refreshTokenUuid)
			.getAppToken();

		mailService.sendAuthenticationMail(member.getEmail(), newAppToken);
	}

	@Override
	public Member getMemberFromAppToken(String appToken) {
		String appTokenUuid = convertAppTokenToAppTokenUuid(appToken);
		Session session = sessionService.findByAppTokenUuid(appTokenUuid);
		return session.getMember();
	}

	private String convertAppTokenToAppTokenUuid(String appToken) {
		AuthToken authToken = authTokenProvider.convertAuthToken(appToken);
		Claims appClaims = authToken.getAppTokenClaims();
		return appClaims.get("jti", String.class);
	}

	@Override
	public AuthRefreshResponse refreshToken(String appToken, String refreshToken) {
		AuthToken authToken = authTokenProvider.convertAuthToken(appToken, refreshToken);
		validateAuthToken(authToken);
		AuthToken newAuthToken = createAuthToken();
		saveAuthToken(newAuthToken, authToken);
		deleteAuthToken(authToken);
		return AuthRefreshResponse.from(newAuthToken.getAppToken());
	}

	private void validateAuthToken(AuthToken authToken) {
		Claims appClaims = authToken.getAppTokenClaimsIncludingExpired();
		Claims refreshClaims = authToken.getRefreshTokenClaims();

		String appTokenUuid = appClaims.get("jti", String.class);
		String refreshTokenUuid = refreshClaims.get("jti", String.class);

		if (!sessionService.existsByAppTokenUuidAndRefreshTokenUuid(appTokenUuid, refreshTokenUuid)) {
			throw new AuthCustomException("Unissued app token or refresh token");
		}
	}

	private AuthToken createAuthToken() {
		String appTokenUuid = UUID.randomUUID().toString();
		String refreshTokenUuid = UUID.randomUUID().toString();
		return authTokenProvider.createUserAuthToken(appTokenUuid, refreshTokenUuid);
	}

	private void saveAuthToken(AuthToken newAuthToken, AuthToken authToken) {
		Claims newAppClaims = newAuthToken.getAppTokenClaims();
		Claims appClaims = authToken.getAppTokenClaimsIncludingExpired();
		Claims refreshClaims = authToken.getRefreshTokenClaims();

		String newAppTokenUuid = newAppClaims.get("jti", String.class);
		String appTokenUuid = appClaims.get("jti", String.class);
		String refreshTokenUuid = refreshClaims.get("jti", String.class);

		Session session = sessionService.findByAppTokenUuid(appTokenUuid);

		Member member = session.getMember();
		Session newSession = Session.builder()
			.member(member)
			.appTokenUuid(newAppTokenUuid)
			.refreshTokenUuid(refreshTokenUuid)
			.build();

		sessionService.save(newSession);
	}

	private void deleteAuthToken(AuthToken authToken) {
	}

	@Override
	public void deleteSession(Member member) {
		List<Session> sessions = sessionRepository.findByMember(member);
		sessionRepository.deleteAll(sessions);
	}
}

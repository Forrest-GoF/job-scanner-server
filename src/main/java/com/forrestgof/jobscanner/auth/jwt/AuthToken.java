package com.forrestgof.jobscanner.auth.jwt;

import java.security.Key;
import java.util.Date;

import com.forrestgof.jobscanner.auth.enumerate.RoleType;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AuthToken {

	private final String token;
	private final Key key;

	private static final String AUTHORITIES_KEY = "role";

	public AuthToken(String socialId, RoleType roleType, Date expiry, Key key) {
		String role = roleType.toString();
		log.trace(role);

		this.key = key;
		this.token = createAuthToken(socialId, role, expiry);
	}

	private String createAuthToken(String socialId, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(socialId) //토큰 제목 설정
			.claim(AUTHORITIES_KEY, role) //사용자 정의 클레임
			.signWith(key, SignatureAlgorithm.HS256) //암호화 복호화 Key
			.setExpiration(expiry) //토큰 만료 시간 설정
			.compact(); //토큰 생성
	}
}

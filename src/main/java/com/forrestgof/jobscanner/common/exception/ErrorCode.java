package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// COMMON
	INVALID_CODE(HttpStatus.valueOf(400), "Invalid Code"),
	RESOURCE_NOT_FOUND(HttpStatus.valueOf(204), "Resource not found"),
	EXPIRED_CODE(HttpStatus.valueOf(400), "Expired Code"),

	//AUTH
	INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

	// MEMBER
	NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "가입되지 않은 계정입니다.");

	private final HttpStatus status;
	private final String message;
}

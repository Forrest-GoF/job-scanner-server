package com.forrestgof.jobscanner.member.exception;

public class NotFoundMemberException extends RuntimeException {

	public NotFoundMemberException() {
		super("회원 가입이 되어 있지 않습니다.");
	}

	public NotFoundMemberException(String message) {
		super(message);
	}
}

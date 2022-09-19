package com.forrestgof.jobscanner.member.exception;

import org.springframework.http.HttpStatus;

import com.forrestgof.jobscanner.common.exception.CustomException;

public class MemberCustomException extends CustomException {
	public MemberCustomException(String message) {
		super(message);
	}

	public MemberCustomException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}

	public static MemberCustomException notfound() {
		return new MemberCustomException("Not found Member", HttpStatus.NOT_FOUND);
	}
}

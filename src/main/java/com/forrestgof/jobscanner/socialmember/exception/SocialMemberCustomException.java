package com.forrestgof.jobscanner.socialmember.exception;

import org.springframework.http.HttpStatus;

import com.forrestgof.jobscanner.common.exception.CustomException;

public class SocialMemberCustomException extends CustomException {

	public SocialMemberCustomException() {
	}

	public SocialMemberCustomException(String message) {
		super(message);
	}

	public SocialMemberCustomException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}
}

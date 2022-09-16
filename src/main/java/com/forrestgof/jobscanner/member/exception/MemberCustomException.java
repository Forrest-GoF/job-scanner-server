package com.forrestgof.jobscanner.member.exception;

import com.forrestgof.jobscanner.common.exception.CustomException;

public class MemberCustomException extends CustomException {
	public MemberCustomException(String message) {
		super(message);
	}

	public static MemberCustomException notfound() {
		return new MemberCustomException("Not found Member");
	}
}
